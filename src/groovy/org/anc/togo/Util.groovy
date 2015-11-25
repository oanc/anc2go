package org.anc.togo

import org.anc.togo.db.Job

import java.security.MessageDigest

/**
 * @author Keith Suderman
 */
class Util {
    static String hash(String input) {
        byte[] bytesOfMessage = input.getBytes("UTF-8")
        MessageDigest md = MessageDigest.getInstance("MD5")
        byte[] bytes = md.digest(bytesOfMessage)
        def hash = String.format("%16x", new BigInteger(1, bytes))
        return hash
    }

    static String getZipFilename(Job job) {
        String name = job.key.replace(':', '-').substring(0, job.key.length() - 1)
        return hash(name) + ".zip"
    }
}
