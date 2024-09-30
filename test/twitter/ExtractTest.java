/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);

    @Test
    public void testGetTimespanEmptyList() {
        // Ensure timespan from now to now is returned for an empty list
        Timespan timespan = Extract.getTimespan(Arrays.asList());
        assertEquals("expected start and end to be the same", timespan.getStart(), timespan.getEnd());
        // This test verifies that the method returns a timespan of "now" to "now" when the input list is empty.
    }
    
    @Test
    public void testGetTimespanSingleTweet() {
        // Test timespan with a single tweet; start and end should be the same.
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d1, timespan.getEnd());
        // This checks that a single tweet returns its timestamp for both start and end of the timespan.
    }

    @Test
    public void testGetTimespanTwoTweets() {
        // Test timespan with two tweets; should return the earliest and latest timestamps.
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
        // This checks that the start and end of the timespan reflect the timestamps of both tweets correctly.
    }

    @Test
    public void testGetTimespanMultipleTweets() {
        // Test timespan with multiple tweets across different timestamps.
        Instant d3 = Instant.parse("2016-02-17T12:00:00Z");
        Tweet tweet3 = new Tweet(3, "user1", "Third tweet", d3);
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2, tweet3));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d3, timespan.getEnd());
        // This verifies that the method captures the range from the earliest to the latest tweet timestamp.
    }

    @Test
    public void testGetMentionedUsersNoMention() {
        // Test with no mentions in a tweet; should return an empty set.
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        assertTrue("expected empty set", mentionedUsers.isEmpty());
        // This checks that no mentions are detected when the tweet contains none.
    }

    @Test
    public void testGetMentionedUsersSingleMention() {
        // Test with a single valid mention; should return that user.
        Tweet tweetWithMention = new Tweet(4, "user", "Hello @alyssa!", d2);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetWithMention));
        assertEquals("expected mentioned user", Set.of("alyssa"), mentionedUsers);
        // This verifies that a single mention is correctly identified and returned in a case-insensitive manner.
    }

    @Test
    public void testGetMentionedUsersMultipleMentions() {
        // Test with multiple valid mentions in a single tweet.
        Tweet tweet = new Tweet(5, "user", "Talk to @alyssa and @bbitdiddle!", d2);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet));
        assertEquals("expected mentioned users", Set.of("alyssa", "bbitdiddle"), mentionedUsers);
        // This checks that multiple mentions are correctly captured and returned in a set.
    }

    @Test
    public void testGetMentionedUsersIgnoreInvalidUsernames() {
        // Test a tweet with invalid mentions (like emails); should ignore invalid formats.
        Tweet tweet = new Tweet(6, "user", "Contact me at user@example.com or @valid_user!", d2);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet));
        assertEquals("expected mentioned users", Set.of("valid_user"), mentionedUsers);
        // This verifies that only valid Twitter usernames are returned, ignoring invalid formats.
    }

    @Test
    public void testGetMentionedUsersCaseInsensitivity() {
        // Test that mentions are case insensitive; should normalize to lower case.
        Tweet tweet = new Tweet(7, "user", "Hello @Alyssa!", d2);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet));
        assertEquals("expected mentioned user", Set.of("alyssa"), mentionedUsers);
        // This checks that the case of the username does not affect detection.
    }

    @Test
    public void testGetMentionedUsersMultipleTweetsWithMentions() {
        // Test with multiple tweets, some with mentions; should return unique users.
        Tweet tweet1 = new Tweet(8, "user1", "Shoutout to @alyssa!", d1);
        Tweet tweet2 = new Tweet(9, "user2", "Thanks @bbitdiddle!", d2);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1, tweet2));
        assertEquals("expected mentioned users", Set.of("alyssa", "bbitdiddle"), mentionedUsers);
        // This ensures that mentions from multiple tweets are correctly aggregated without duplicates.
    }

    @Test
    public void testGetMentionedUsersMixedContent() {
        // Test a tweet with mixed content; should only return valid mentions.
        Tweet tweet = new Tweet(10, "user", "This is invalid@ and this is @valid_user!", d2);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet));
        assertEquals("expected mentioned user", Set.of("valid_user"), mentionedUsers);
        // This verifies that invalid formats are ignored while valid mentions are detected.
    }

    @Test
    public void testGetTimespanIdenticalTimestamps() {
        // Test with tweets having identical timestamps; should return the same timestamp for start and end.
        Instant sameTime = Instant.parse("2016-02-17T10:00:00Z");
        Tweet tweet1 = new Tweet(11, "user", "Tweet 1", sameTime);
        Tweet tweet2 = new Tweet(12, "user", "Tweet 2", sameTime);
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        assertEquals("expected start", sameTime, timespan.getStart());
        assertEquals("expected end", sameTime, timespan.getEnd());
        // This checks that when multiple tweets have the same timestamp, it returns that timestamp for both start and end.
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */
}
