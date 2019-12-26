package com.muye.monitor.agent.lib;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class LibClassLoader extends ClassLoader {

    private static final String LIB_JAR_PATH = System.getProperty("user.dir") + "/" + "monitor-lib.jar";

    private Map<String, byte[]> classCache = new HashMap<>();

    private static final LibClassLoader instance = new LibClassLoader();

    private LibClassLoader(){
        super();
        cacheClass();
    }

    public static LibClassLoader getLibClassLoader(){

        return instance;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        byte[] bytes = classCache.get(name);
        if (bytes == null) {
            throw new ClassNotFoundException(name);
        }

        return defineClass(name, bytes, 0, bytes.length);
    }

    private JarFile getJarFile(){
        File file = new File(LIB_JAR_PATH);
        if (file.exists()) {
            try {
                return new JarFile(file);
            } catch (IOException e) {
                throw new RuntimeException("读取monitor-lib.jar失败", e);
            }
        }

        throw new RuntimeException("没有获取到monitor-lib.jar");
    }

    private void cacheClass() {
        JarFile jarFile = getJarFile();
        Enumeration<JarEntry> en = jarFile.entries();
        while (en.hasMoreElements()) {
            JarEntry entry = en.nextElement();
            String name = entry.getName();
            if (!name.endsWith(".class")) {
                continue;
            }

            String className = name.substring(0, name.length() - 6).replace("/", ".");

            if (findLoadedClass(className) != null) {
                continue;
            }

            InputStream is = null;
            try {
                is = jarFile.getInputStream(entry);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int numRead;
                while ((numRead = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, numRead);
                }
                byte[] cc = baos.toByteArray();
                classCache.put(className, cc);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }
}
