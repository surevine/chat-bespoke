package com.surevine.chat.view.linker;

import java.util.SortedSet;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.linker.ConfigurationProperty;
import com.google.gwt.core.ext.linker.PropertyProviderGenerator;
import com.google.gwt.user.rebind.StringSourceWriter;

/**
 * Generates a property provider to allow the transparent PNG bodge in IE6 to be disabled.
 * It is controlled by two globally scoped JS variables:
 * <dl>
 *      <dt><code>disableIE6PNGFix</code></dt>
 *      <dd>If set to <code>true</code> then the transparent PNG fix will always be disabled in IE6</dd>
 *      
 *      <dt><code>disableIE6PNGFixUAs</code></dt>
 *      <dd>Contains an array of user agent string fragments (in lower case) which will be used to
 *              disable the transparent PNG fix if <code>disableIE6PNGFix</code> is set to false.</dd>
 * </dl>
 */
public class IE6ImagePropertyProviderGenerator implements PropertyProviderGenerator {

    @Override
    public String generate(TreeLogger logger, SortedSet<String> possibleValues,
            String fallback, SortedSet<ConfigurationProperty> configProperties)
            throws UnableToCompleteException {
        StringSourceWriter body = new StringSourceWriter();
        
        body.println("{");
        body.indent();
        body.println("try {");
        body.indent();
        body.println("if(disableIE6PNGFix) { return 'false' };");
        body.println();
        body.println("var ua = navigator.userAgent.toLowerCase();");
        body.println();
        body.println("if(disableIE6PNGFixUAs && disableIE6PNGFixUAs.length) {");
        body.indent();
        body.println("for (var i = 0; i < disableIE6PNGFixUAs.length; ++i) {");
        body.indent();
        body.println("if(ua.indexOf(disableIE6PNGFixUAs[i]) != -1) { return 'false'; }");
        body.outdent();
        body.println("}");
        body.outdent();
        body.println("}");
        body.outdent();
        body.println("} catch(e) { } // Ignore");
        body.println();
        body.println("return 'true';");
        body.outdent();
        body.println("}");

        return body.toString();
    }
}
