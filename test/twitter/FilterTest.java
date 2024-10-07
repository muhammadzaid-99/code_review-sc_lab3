/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "alyssa", "loving this discussion about algorithms", d3);

//    @Test(expected = AssertionError.class)
//    public void testAssertionsEnabled() {
//        assert false; // make sure assertions are enabled with VM argument: -ea
//    }

    // Test cases for writtenBy
    @Test
    public void testWrittenByMultipleTweetsSingleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3), "alyssa");
        assertEquals("expected singleton list", 2, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet3));
    }
 
    @Test
    public void testWrittenByNoTweets() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "unknown");
        assertTrue("expected empty list", writtenBy.isEmpty());
    }

    @Test
    public void testWrittenByCaseInsensitive() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "ALYSSA");
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }

    // Test cases for inTimespan
    @Test
    public void testInTimespanMultipleTweetsMultipleResults() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");

        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3), new Timespan(testStart, testEnd));
        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet2, tweet3)));
    }

    @Test
    public void testInTimespanNoTweetsWithinRange() {
        Instant testStart = Instant.parse("2016-02-17T13:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T14:00:00Z");

        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));
        assertTrue("expected empty list", inTimespan.isEmpty());
    }

    @Test
    public void testInTimespanPartialTweets() {
        Instant testStart = Instant.parse("2016-02-17T10:30:00Z");
        Instant testEnd = Instant.parse("2016-02-17T11:30:00Z");

        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3), new Timespan(testStart, testEnd));
        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweet", inTimespan.contains(tweet2));
        assertFalse("expected list not to contain tweet", inTimespan.contains(tweet1));
        assertFalse("expected list not to contain tweet", inTimespan.contains(tweet3));
    }

    // Test cases for containing
    @Test
    public void testContainingWithMatchingWords() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2)));
    }

    @Test
    public void testContainingWithNoMatchingWords() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("nonexistent"));
        assertTrue("expected empty list", containing.isEmpty());
    }

    @Test
    public void testContainingWithMixedCase() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("TALK"));
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2)));
    }
}
