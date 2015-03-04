/*
 * The MIT License
 *
 * Copyright (c) 2014 The Broad Institute
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.broadinstitute.hellbender.tools.picard.sam.markduplicates;

import org.broadinstitute.hellbender.exceptions.GATKException;
import org.broadinstitute.hellbender.exceptions.UserException;
import org.broadinstitute.hellbender.utils.sam.markduplicates.AbstractMarkDuplicatesCommandLineProgramTest;
import org.broadinstitute.hellbender.utils.sam.markduplicates.AbstractMarkDuplicatesTester;
import org.broadinstitute.hellbender.utils.sam.markduplicates.MarkDuplicatesWithMateCigarTester;
import org.testng.annotations.Test;

/**
 * This class defines the individual test cases to run. The actual running of the test is done
 * by MarkDuplicatesWithMateCigarTester (see getTester).
 * @author nhomer@broadinstitute.org
 */
public class MarkDuplicatesWithMateCigarIntegrationTest extends AbstractMarkDuplicatesCommandLineProgramTest {
    protected AbstractMarkDuplicatesTester getTester() {
        return new MarkDuplicatesWithMateCigarTester();
    }

    // TODO: test program record chaining, including failures. Use MarkDuplicate's facility.
    // TODO: check if one mate is dup, the other is as well, only if both are mapped

    // NB: this test should return different results than MarkDuplicatesWithMateCigar, as we have the mate cigar
    @Test
    public void testTwoMappedPairsWithSoftClippingFirstOfPairOnly() {
        final AbstractMarkDuplicatesTester tester = getTester();
        // NB: no duplicates
        // 5'1: 2, 5'2:46+73M=118
        // 5'1: 2, 5'2:51+68M=118
        tester.addMappedPair(0, 12, 46, false, false, "6S42M28S", "3S73M", true, 50); // only add the first one
        // NB: this next record should not be a duplicate in MarkDuplicates, but is here, because have the mate cigar
        tester.addMappedPair(0, 12, 51, true, true, "6S42M28S", "8S68M", true, 50); // only add the first one
        tester.runTest();
    }

    @Test
    public void testTwoFragmentsLargeSoftClipWithMinimumDistanceOK() {
        final AbstractMarkDuplicatesTester tester = getTester();
        tester.addArg("MINIMUM_DISTANCE=990");
        tester.addMappedFragment(0, 1000, false, "100M", DEFAULT_BASE_QUALITY);
        tester.addMappedFragment(0, 2000, false, "10S100M", DEFAULT_BASE_QUALITY);
        tester.addMappedFragment(0, 3000, true, "2000S100M", DEFAULT_BASE_QUALITY);
        tester.runTest();
    }

    @Test(expectedExceptions = {GATKException.class, UserException.class})
    public void testTwoFragmentsLargeSoftClipWithMinimumDistanceFailure() {
        final AbstractMarkDuplicatesTester tester = getTester();
        tester.addArg("MINIMUM_DISTANCE=989");
        tester.addMappedFragment(0, 1000, false, "100M", DEFAULT_BASE_QUALITY);
        tester.addMappedFragment(0, 2000, false, "10S100M", DEFAULT_BASE_QUALITY);
        tester.addMappedFragment(0, 3000, true, "2000S100M", DEFAULT_BASE_QUALITY);
        tester.runTest();
    }

    @Test(expectedExceptions = {GATKException.class, UserException.class})
    public void testTwoFragmentsLargeSoftClip() {
        final AbstractMarkDuplicatesTester tester = getTester();
        tester.addMappedFragment(0, 1000, false, "100M", DEFAULT_BASE_QUALITY);
        tester.addMappedFragment(0, 2000, false, "10S100M", DEFAULT_BASE_QUALITY);
        tester.addMappedFragment(0, 3000, true, "2000S100M", DEFAULT_BASE_QUALITY);
        tester.runTest();
    }

    @Test
     public void testScoringStrategyForReadNameComparison() {
        final AbstractMarkDuplicatesTester tester = getTester();
        tester.addMappedFragment(0, 1, false, DEFAULT_BASE_QUALITY);  // Ref lengths, MapQs equal. First read name in lex order called dup.
        tester.addMappedFragment(0, 1, true, DEFAULT_BASE_QUALITY);
        tester.runTest();
    }

    @Test
    public void testScoringStrategyForMateReferenceLengthComparison() {
        final AbstractMarkDuplicatesTester tester = getTester();

        // READY pair are both duplicates because (sum of reference length) for both reads is less than for READX
        // MarkDuplicates and SUM_OF_BASE_QUALITIES scoring strategy would mark READX pair a duplicate, as all reads have equal quals
        // If this scoring strategy did not account for mate reference length, READX pair would be marked a duplicate
        tester.addMatePair("READY", 1, 1, 105, false, false, true, true, "50M", "5I45M", false, true, false,
                false, false, DEFAULT_BASE_QUALITY); // duplicate pair. Both reads should be duplicates!!!
        tester.addMatePair("READX", 1, 1, 100, false, false, false, false, "50M", "50M", false, true, false,
                false, false, DEFAULT_BASE_QUALITY);

        tester.runTest();
    }
}