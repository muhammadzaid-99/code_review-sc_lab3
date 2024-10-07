/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.time.Instant;
import java.util.stream.Collectors;

/**
 * Filter consists of methods that filter a list of tweets for those matching a
 * condition.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Filter {
 
    /**
     * Find tweets written by a particular user.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param username
     *            Twitter username, required to be a valid Twitter username as
     *            defined by Tweet.getAuthor()'s spec.
     * @return all and only the tweets in the list whose author is username,
     *         in the same order as in the input list.
     */
	public static List<Tweet> writtenBy(List<Tweet> tweets, String username) {
	    List<Tweet> result = new ArrayList<>();
	    for (Tweet tweet : tweets) {
//	        if (tweet.getAuthor().equalsIgnoreCase(username)) {
	    	// Added Bug : no longer case insensitive
        	if (tweet.getAuthor().equals(username)) {
	            result.add(tweet);
	        }
	    }
	    return result;
	}

    /**
     * Find tweets that were sent during a particular timespan.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param timespan
     *            timespan
     * @return all and only the tweets in the list that were sent during the timespan,
     *         in the same order as in the input list.
     */
	public static List<Tweet> inTimespan(List<Tweet> tweets, Timespan timespan) {
	    List<Tweet> result = new ArrayList<>();
	    Instant start = timespan.getStart();
	    Instant end = timespan.getEnd();

	    for (Tweet tweet : tweets) {
	        Instant timestamp = tweet.getTimestamp();
	        if (!timestamp.isBefore(start) && !timestamp.isAfter(end)) {
	            result.add(tweet);
	        }
	    }
	    return result;
	}

    /**
     * Find tweets that contain certain words.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param words
     *            a list of words to search for in the tweets. 
     *            A word is a nonempty sequence of nonspace characters.
     * @return all and only the tweets in the list such that the tweet text (when 
     *         represented as a sequence of nonempty words bounded by space characters 
     *         and the ends of the string) includes *at least one* of the words 
     *         found in the words list. Word comparison is not case-sensitive,
     *         so "Obama" is the same as "obama".  The returned tweets are in the
     *         same order as in the input list.
     */
	
	// This is original implementation
	public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
	    Set<String> wordSet = new HashSet<>();
	    for (String word : words) {
	        wordSet.add(word.toLowerCase());
	    }

	    List<Tweet> result = new ArrayList<>();
	    for (Tweet tweet : tweets) {
	        String[] tweetWords = tweet.getText().toLowerCase().split("\\s+");
	        for (String tweetWord : tweetWords) {
	            if (wordSet.contains(tweetWord)) {
	                result.add(tweet);
	                break; // Stop searching once we find a match
	            }
	        }
	    }
	    return result;
	} 
	

	// Implementation variant 1: using streams
//	public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
//        Set<String> wordSet = words.stream()
//            .map(String::toLowerCase)
//            .collect(Collectors.toSet());
//
//        return tweets.stream()
//            .filter(tweet -> {
//                String[] tweetWords = tweet.getText().toLowerCase().split("\\s+");
//                for (String tweetWord : tweetWords) {
//                    if (wordSet.contains(tweetWord)) {
//                        return true; // Tweet contains at least one matching word
//                    }
//                }
//                return false; // No matching words found
//            })
//            .collect(Collectors.toList());
//    }
	
	// implementation variant 2, using regex
//	 public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
//        Set<String> wordSet = new HashSet<>();
//        for (String word : words) {
//            wordSet.add(word.toLowerCase());
//        }
//
//        return tweets.stream()
//            .filter(tweet -> {
//                String tweetTextLower = tweet.getText().toLowerCase();
//                return wordSet.stream().anyMatch(word -> tweetTextLower.matches(".*\\b" + word + "\\b.*"));
//            })
//            .collect(Collectors.toList());
//    }
	
	// implementation v3: using nested loop with early exit
//	public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
//        Set<String> wordSet = new HashSet<>();
//        for (String word : words) {
//            wordSet.add(word.toLowerCase());
//        }
//
//        List<Tweet> result = new ArrayList<>();
//        for (Tweet tweet : tweets) {
//            String tweetTextLower = tweet.getText().toLowerCase();
//            String[] tweetWords = tweetTextLower.split("\\s+");
//            for (String tweetWord : tweetWords) {
//                if (wordSet.contains(tweetWord)) {
//                    result.add(tweet);
//                    break; // Early exit upon finding a match
//                }
//            }
//        }
//        return result;
//    }
 
} 
