package net.gtn.dimensionalpocket.client.utils.version;

import net.gtn.dimensionalpocket.common.core.utils.DPLogger;

public class Version {
    public final String version;
    public final String url;
    public final String changelog;
    public final String additionalInfo;
    
    public Version(String version, String url, String changelog, String additionalInfo) {
        this.version = version;
        this.url = url;
        this.changelog = changelog;
        this.additionalInfo = additionalInfo;
    }
    
    public boolean isNewerThan(Version v) {
        String[] parts1 = version.split("\\.");
        String[] parts2 = v.version.split("\\.");
        
        for (int i=0; i<parts1.length && i<parts2.length; i++) {
            String part1 = parts1[i];
            String part2 = parts2[i];
            
            // strip leading zeroes
            while (part1.length()>1 && part1.startsWith("0")) {
                part1 = part1.substring(1);
            }
            while (part2.length()>1 && part2.startsWith("0")) {
                part2 = part2.substring(1);
            }
            
            int num1 = 0;
            int num2 = 0;
            
            try {
                num1 = Integer.parseInt(part1);
            } catch (NumberFormatException nfe) {
                DPLogger.warning("\"" + version + "\" is not a valid version!", Version.class);
                return false;
            }
            
            try {
                num2 = Integer.parseInt(part2);
            } catch (NumberFormatException nfe) {
                DPLogger.warning("\"" + v.version + "\" is not a valid version!", Version.class);
                return true;
            }
            
            if (num1 != num2)
                return num1 > num2;
        }
        
        // if no difference was found so far, the version with the higher parts number is the more recent one
        return parts1.length > parts2.length;
    }
}
