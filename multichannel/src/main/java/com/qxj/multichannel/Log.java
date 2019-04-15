//package com.qxj.multichannel;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Environment;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//
//public class Log {
//
//    private void start(Context context) {
//        //通过反射修改配置 loggerlauncher
//        set("ctl.start", "loggerlauncher");
//        set("debug.dbgcfgtool.enabled", "true");
//        //发送log配置改变广播
//        context.sendBroadcast(new Intent("com.evenwell.DbgCfgTool.ACTION_LOG_CONFIG_CHANGE"));
//    }
//
//    private void stop(Context context) {
//        //通过反射修改配置
//        set("ctl.start", "logcatkill");
//        set("debug.dbgcfgtool.enabled", "false");
//        //发送log配置改变广播
//        context.sendBroadcast(new Intent("com.evenwell.DbgCfgTool.ACTION_LOG_CONFIG_CHANGE"));
//    }
//
//    private void set(String str, String str2) {
//        methodWithReturn("android.os.SystemProperties", "set", str, str2);
//    }
//
//    private Object methodWithReturn(String str, String str2, Object... objArr) {
//        ArgsClass argConvert = argConvert(objArr);
//        try {
//            Method method = Class.forName(str).getMethod(str2, argConvert.objClass);
//            method.setAccessible(true);
//            return method.invoke(null, argConvert.objData);
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    private ArgsClass argConvert(Object... objArr) {
//        ArgsClass argsClass = new ArgsClass();
//        argsClass.objClass = new Class[objArr.length];
//        argsClass.objData = new Object[objArr.length];
//        for (int i = 0; i < objArr.length; i++) {
//            if (mPrimitiveMap.containsKey(objArr[i].getClass())) {
//                argsClass.objClass[i] = (Class) mPrimitiveMap.get(objArr[i].getClass());
//            } else {
//                argsClass.objClass[i] = objArr[i].getClass();
//            }
//            argsClass.objData[i] = objArr[i];
//        }
//        return argsClass;
//    }
//
//    private static class ArgsClass {
//        Class[] objClass;
//        Object[] objData;
//
//        private ArgsClass() {
//        }
//    }
//
//    private static HashMap<Class, Class> mPrimitiveMap = new HashMap();
//
//    static {
//        mPrimitiveMap.put(Integer.class, Integer.TYPE);
//        mPrimitiveMap.put(Long.class, Long.TYPE);
//        mPrimitiveMap.put(Boolean.class, Boolean.TYPE);
//        mPrimitiveMap.put(Byte.class, Byte.TYPE);
//        mPrimitiveMap.put(Character.class, Character.TYPE);
//        mPrimitiveMap.put(Short.class, Short.TYPE);
//        mPrimitiveMap.put(Float.class, Float.TYPE);
//        mPrimitiveMap.put(Double.class, Double.TYPE);
//    }
//
//
//    private void startCopy() {
//        String path = Environment.getExternalStorageDirectory().getPath() + "/...";//SD卡根目录
////监听广播
////"android.intent.action.BUGREPORT_CANCEL"0
////"com.android.internal.intent.action.BUGREPORT_FINISHED";
////"android.intent.action.BUGREPORT_FINISHED";
////"com.android.internal.intent.action.BUGREPORT_STARTED";
////"android.intent.action.BUGREPORT_STARTED";
//        callBugreport(, "");
//        copyToSdcard2(path);
//        copyFolder("/sdcard/tcpdump", path);
//
//    }
//
//    public void callBugreport(BugreportReceiver bugreportReceiver, String str) {
//        set("debug.bugreport.dest", str);
//        try {
//            bugreportReceiver.start(mContext);
//            set("ctl.start", "bugreport");
//            bugreportReceiver.setStartBlockerStatus(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public boolean copyToSdcard2(String rootDirPath) {
//        ArrayList arrayList = new ArrayList();
//        arrayList.add(new LogCopyItem("/data/anr/", rootDirPath + "/trace", null));
//        arrayList.add(new LogCopyItem("/data/logs/", rootDirPath + "/logs/data", "alog(.*)"));
//        arrayList.add(new LogCopyItem("/data/tombstones/", rootDirPath + "/tombstone", "tombstone(.*)"));
//        arrayList.add(new LogCopyItem("/data/system/dropbox/", rootDirPath + "/dropbox", null));
//        arrayList.add(new LogCopyItem("/data/local/tmp/MKY_Log", rootDirPath + "/MKY_LOG", null));
//        arrayList.add(new LogCopyItem("/data/MKY_LOG", rootDirPath + "/MKY_LOG2", null));
//        arrayList.add(new LogCopyItem("/data/vendor/diag_logs/log/", rootDirPath + "/diag_logs", null));
//        arrayList.add(new LogCopyItem("/data/light/", rootDirPath + "/asic_log", "asic_1.log"));
//        arrayList.add(new LogCopyItem("/data/vendor/light/", rootDirPath + "/asic_log", "asic_1.log"));
//        Iterator it = arrayList.iterator();
//        while (it.hasNext()) {
//            LogCopyItem logCopyItem = (LogCopyItem) it.next();
//            //copyLogFile(new File(logCopyItem.sourceDir), new File(logCopyItem.destDir), logCopyItem.fileKeyword);
//            copyFolder(logCopyItem.sourceDir, logCopyItem.destDir);
//        }
//        return true;
//    }
//
//    public int copyFolder(String str, String str2) {
//        int i = 0;
//        File file = new File(str);
//        File file2 = new File(str2);
//        int i2 = 0;
//        StringBuilder stringBuilder;
//        if (file.exists()) {
//            File[] listFiles = file.listFiles();
//            if (listFiles != null && listFiles.length > 0) {
//                int length = listFiles.length;
//                i = 0;
//                while (i2 < length) {
//                    File file3 = listFiles[i2];
//                    File file4 = new File(str2, file3.getName());
//                    if (file3.isFile()) {
//                        if (!file2.exists()) {
//                            file2.mkdirs();
//                        }
//                        copyFile(file3.getPath(), file4.getPath());
//                        i++;
//                    } else {
//                        i += copyFolder(file3.getPath(), file4.getPath());
//                    }
//                    i2++;
//                }
//            }
//            return i;
//        }
//        return i;
//    }
//
//    public static void copyFile(String str, String str2) {
//        String str3;
//        StringBuilder stringBuilder;
//        try {
//            File file = new File(str);
//            File file2 = new File(str2);
//            FileInputStream fileInputStream = new FileInputStream(file);
//            FileOutputStream fileOutputStream = new FileOutputStream(file2);
//            byte[] bArr = new byte[1024];
//            while (true) {
//                int read = fileInputStream.read(bArr);
//                if (read > 0) {
//                    fileOutputStream.write(bArr, 0, read);
//                } else {
//                    fileInputStream.close();
//                    fileOutputStream.close();
//                    file2.setReadable(true, false);
//                    file2.setWritable(true, false);
//                    return;
//                }
//            }
//        } catch (FileNotFoundException unused) {
//            android.util.Log.d("copy", "FileNotFoundException occurred when copy " + str + " to " + str2);
//        } catch (IOException unused2) {
//            android.util.Log.d("copy", "IOException occurred when copy " + str + " to " + str2);
//        }
//    }
//
//    static class LogCopyItem {
//        public String destDir;
//        public String fileKeyword;
//        public String sourceDir;
//
//        public LogCopyItem(String str, String str2, String str3) {
//            this.sourceDir = str;
//            this.destDir = str2;
//            this.fileKeyword = str3;
//        }
//    }
//}
