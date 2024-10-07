/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

	/**
	 * Get the time period spanned by tweets.
	 * 
	 * @param tweets list of tweets with distinct ids, not modified by this method.
	 * @return a minimum-length time interval that contains the timestamp of every
	 *         tweet in the list.
	 */

	// This is the orignal variant

		public static Timespan getTimespan(List<Tweet> tweets) {
			
			// Added Bug : incorrect handling of empty list, returns wring timestamp
		    if (tweets.isEmpty()) {
		    	return new Timespan(Instant.now().minusSeconds(3600), Instant.now());
//		        return new Timespan(Instant.now(), Instant.now());
		    }
		    
		    Instant start = tweets.get(0).getTimestamp();
		    Instant end = start;
		     
		    for (Tweet tweet : tweets) {
		        Instant timestamp = tweet.getTimestamp();
		        if (timestamp.isBefore(start)) {
		            start = timestamp;
		        }
		        if (timestamp.isAfter(end)) {
		            end = timestamp;
		        }
		    } 
		     
		    return new Timespan(start, end);
		}
	
	
	// This is the first implementation variant
//	public static Timespan getTimespan(List<Tweet> tweets) {
//        if (tweets.isEmpty()) {
//            return new Timespan(Instant.now(), Instant.now());
//        }
//
//        Instant start = Instant.MAX; // max time
//        Instant end = Instant.MIN; // min time
//
//        for (Tweet tweet : tweets) {
//            Instant timestamp = tweet.getTimestamp();
//            if (timestamp.isBefore(start)) {
//                start = timestamp;
//            }
//            if (timestamp.isAfter(end)) {
//                end = timestamp;
//            }
//        }
//
//        return new Timespan(start, end);
//    }
	
	// This is the second implementation variant using collectors
//	public static Timespan getTimespan(List<Tweet> tweets) {
//        if (tweets.isEmpty()) {
//            return new Timespan(Instant.now(), Instant.now());
//        }
//
//        // Use collectors to gather timestamps into a list
//        List<Instant> timestamps = tweets.stream()
//            .map(Tweet::getTimestamp)
//            .collect(Collectors.toList());
//
//        Instant start = timestamps.stream().min(Instant::compareTo).orElse(Instant.now());
//        Instant end = timestamps.stream().max(Instant::compareTo).orElse(Instant.now());
//
//        return new Timespan(start, end);
//    }
	
	// This is the third implementation variant using sorted sets
//	 public static Timespan getTimespan(List<Tweet> tweets) {
//        if (tweets.isEmpty()) {
//            return new Timespan(Instant.now(), Instant.now());
//        }
//
//        TreeSet<Instant> timestamps = new TreeSet<>();
//        for (Tweet tweet : tweets) {
//            timestamps.add(tweet.getTimestamp());
//        }
//
//        return new Timespan(timestamps.first(), timestamps.last());
//    }


	/**
	 * Get usernames mentioned in a list of tweets.
	 * 
	 * @param tweets list of tweets with distinct ids, not modified by this method.
	 * @return the set of usernames who are mentioned in the text of the tweets. A
	 *         username-mention is "@" followed by a Twitter username (as defined by
	 *         Tweet.getAuthor()'s spec). The username-mention cannot be immediately
	 *         preceded or followed by any character valid in a Twitter username.
	 *         For this reason, an email address like bitdiddle@mit.edu does NOT
	 *         contain a mention of the username mit. Twitter usernames are
	 *         case-insensitive, and the returned set may include a username at most
	 *         once.
	 */
	public static Set<String> getMentionedUsers(List<Tweet> tweets) {
		Set<String> mentionedUsers = new HashSet<>();
		Pattern pattern = Pattern.compile("(?<!\\w)@([a-zA-Z0-9_]+)(?!\\w)");

		for (Tweet tweet : tweets) {
			Matcher matcher = pattern.matcher(tweet.getText());
			while (matcher.find()) {
				mentionedUsers.add(matcher.group(1).toLowerCase());
			}
		}

		return mentionedUsers;
	}

}
