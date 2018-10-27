package org.shininet.bukkit.playerheads;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

@SuppressWarnings("ALL")
class UTF8Control extends Control { // Copied from jdk1.6.0_38
    /**
     * Instantiates a resource bundle for the given bundle name of the given format and locale, using the given class loader if necessary. This method returns <code>null</code> if there is no resource bundle available for the given parameters. If a resource bundle can't be instantiated due to an unexpected error, the error must be reported by throwing an <code>Error</code> or <code>Exception</code> rather than simply returning <code>null</code>.
     * <p>
     * If the <code>reload</code> flag is <code>true</code>, it indicates that this method is being called because the previously loaded resource bundle has expired.
     * </p>
     * <p>
     * The default implementation instantiates a <code>ResourceBundle</code> as follows.
     * </p>
     * <ul>
     * <li>The bundle name is obtained by calling {@link #toBundleName(String, Locale) toBundleName(baseName, locale)}.</li>
     * <li>If <code>format</code> is <code>"java.class"</code>, the {@link Class} specified by the bundle name is loaded by calling {@link ClassLoader#loadClass(String)}. Then, a <code>ResourceBundle</code> is instantiated by calling {@link Class#newInstance()}. Note that the <code>reload</code> flag is ignored for loading class-based resource bundles in this default implementation.</li>
     * <li>If <code>format</code> is <code>"java.properties"</code>, {@link #toResourceName(String, String) toResourceName(bundlename, "properties")} is called to get the resource name. If <code>reload</code> is <code>true</code>, {@link ClassLoader#getResource(String) load.getResource} is called to get a {@link URL} for creating a {@link URLConnection}. This <code>URLConnection</code> is used to {@linkplain URLConnection#setUseCaches(boolean) disable the caches} of the underlying resource loading layers,
     * and to {@linkplain URLConnection#getInputStream() get an <code>InputStream</code>}. Otherwise, {@link ClassLoader#getResourceAsStream(String) loader.getResourceAsStream} is called to get an {@link InputStream}. Then, a {@link PropertyResourceBundle} is constructed with the <code>InputStream</code>.</li>
     * <li>If <code>format</code> is neither <code>"java.class"</code> nor <code>"java.properties"</code>, an <code>IllegalArgumentException</code> is thrown.</li>
     * </ul>
     *
     * @param baseName the base bundle name of the resource bundle, a fully qualified class name
     * @param locale   the locale for which the resource bundle should be instantiated
     * @param format   the resource bundle format to be loaded
     * @param loader   the <code>ClassLoader</code> to use to load the bundle
     * @param reload   the flag to indicate bundle reloading; <code>true</code> if reloading an expired resource bundle, <code>false</code> otherwise
     * @return the resource bundle instance, or <code>null</code> if none could be found.
     * @throws NullPointerException        if <code>bundleName</code>, <code>locale</code>, <code>format</code>, or <code>loader</code> is <code>null</code>, or if <code>null</code> is returned by {@link #toBundleName(String, Locale) toBundleName}
     * @throws IllegalArgumentException    if <code>format</code> is unknown, or if the resource found for the given parameters contains malformed data.
     * @throws ClassCastException          if the loaded class cannot be cast to <code>ResourceBundle</code>
     * @throws IllegalAccessException      if the class or its nullary constructor is not accessible.
     * @throws InstantiationException      if the instantiation of a class fails for some other reason.
     * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
     * @throws SecurityException           If a security manager is present and creation of new instances is denied. See {@link Class#newInstance()} for details.
     * @throws IOException                 if an error occurred when reading resources using any I/O operations
     */
    @SuppressWarnings("unchecked")
    
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException {
        String bundleName = toBundleName(baseName, locale);
        ResourceBundle bundle = null;
        if (format.equals("java.class")) {
            try {
                Class<? extends ResourceBundle> bundleClass = (Class<? extends ResourceBundle>) loader.loadClass(bundleName);

                // If the class isn't a ResourceBundle subclass, throw a ClassCastException.
                if (ResourceBundle.class.isAssignableFrom(bundleClass)) {
                    bundle = bundleClass.newInstance();
                } else {
                    throw new ClassCastException(bundleClass.getName() + " cannot be cast to ResourceBundle");
                }
            } catch (ClassNotFoundException e) {
            }
        } else if (format.equals("java.properties")) {
            final String resourceName = toResourceName(bundleName, "properties");
            final ClassLoader classLoader = loader;
            final boolean reloadFlag = reload;
            InputStream stream = null;
            try {
                
                PrivilegedExceptionAction<InputStream> action = new PrivilegedExceptionAction<InputStream>() {
                    @Override
                    public InputStream run() throws IOException {
                        InputStream is = null;
                        if (reloadFlag) {
                            URL url = classLoader.getResource(resourceName);
                            if (url != null) {
                                URLConnection connection = url.openConnection();
                                if (connection != null) {
                                    // Disable caches to get fresh data for reloading.
                                    connection.setUseCaches(false);
                                    is = connection.getInputStream();
                                }
                            }
                        } else {
                            is = classLoader.getResourceAsStream(resourceName);
                        }
                        return is;
                    }
                };
                stream = AccessController.doPrivileged(action);
                
                
            }catch (PrivilegedActionException e) {
                throw (IOException) e.getException();
            }
            if (stream != null) {
                try {
                    bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8")); // Edited to add UTF-8
                } finally {
                    stream.close();
                }
            }
        } else {
            throw new IllegalArgumentException("unknown format: " + format);
        }
        return bundle;
    }
}
