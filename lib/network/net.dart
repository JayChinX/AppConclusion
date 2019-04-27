import 'dart:io';
import 'package:dio/dio.dart';

BaseOptions _options = new BaseOptions(
    baseUrl: "https://www...", connectTimeout: 5000, receiveTimeout: 3000);

Dio _dio = new Dio(_options);

void setInterceptors() {
  _dio.interceptors
      .add(InterceptorsWrapper(onRequest: (RequestOptions options) {
    //发送请求之前 return options
    return _dio.resolve("可以返回假数据");
  }, onResponse: (Response response) {
    //成功
    return response;
  }, onError: (DioError e) {
    //失败 return e
    return _dio.reject("返回错误提示信息");
  }));
}

class Network {}

class InterceptorNetwork extends Network {
  //GET
  static Future get(String url, {Map<String, dynamic> params}) async {
    var response = await _dio.get(url, queryParameters: params);
    return response.data;
  }

  //POST
  static Future post(String url,
      {Map<String, dynamic> params, Map<String, dynamic> data}) async {
    var response = await _dio.post(url, queryParameters: params, data: data);
    return response.data;
  }

  //POST FORM DATA
  static Future postFormData(String url, Map<String, dynamic> data) async {
    FormData formData = new FormData.from(data);
    var response = await _dio.post(url, data: formData,
        onSendProgress: (int sent, int total) {
      //上传进度
      print("$sent $total");
    });
    return response.data;
  }

  //POST 上传单个文件
  /*
   *  data: 表单数据
   */
  static Future uploadingFile(
      String url, Map<String, dynamic> data, Map<String, String> files) async {
    files.forEach((fileName, path) =>
        data['file'] = new UploadFileInfo(new File(path), fileName));
    return postFormData(url, data);
  }

  //POST 上传多个文件
  static Future uploadingFiles(
      String url, Map<String, dynamic> data, Map<String, String> files) async {
    data['files'] = [];
    files.forEach((fileName, path) =>
        data['files'].add(new UploadFileInfo(new File(path), fileName)));
    return postFormData(url, data);
  }

  //并发请求
  static Future postList() async {
    var response = await Future.wait([_dio.get(''), _dio.post('')]);
  }

  //下载文件
  static Future downloadFile(String urlPath, String savePath) async {
    var response = await _dio.download(urlPath, savePath,
        onReceiveProgress: (int count, int total) {
      //下载进度
      print("$count $total");
    });
    return response.data;
  }

  //GET stream
  static Future getStream(String url) async {
    var response = await _dio.get<ResponseBody>(url,
        options: Options(responseType: ResponseType.stream));
    return response.data.stream;
  }

  //GET BYTES
  static Future getBytes(String url) async {
    var response = await _dio.get<List<int>>(url,
        options: Options(responseType: ResponseType.bytes));
    return response.data;
  }
}
