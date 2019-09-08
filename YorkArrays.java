package A4Q1;

import java.util.*;

/**
 *
 * Provides two static methods for sorting Integer arrays (heapSort and
 * mergeSort)
 * 
 * @author jameselder
 */
public class YorkArrays {

	/*
	 * Sorts the input array of Integers a using HeapSort. Result is returned in a.
	 * Makes use of java.util.PriorityQueue. Sorting is NOT in place - PriorityQueue
	 * allocates a separate heap-based priority queue. Not a stable sort, in
	 * general. Throws a null pointer exception if the input array is null.
	 */
	public static void heapSort(Integer[] a) throws NullPointerException {
		if (a == null) {
			throw new NullPointerException();
		}
		PriorityQueue<Integer> heap = new PriorityQueue<>();
		for (int i = 0; i < a.length; i++) {
			heap.add(a[i]);
		}
		for (int i = 0; i < a.length; i++) {
			a[i] = heap.poll();
		}
	}

	/*
	 * Sorts the input array of Integers a using mergeSort and returns result.
	 * Sorting is stable. Throws a null pointer exception if the input array is
	 * null.
	 */
	public static Integer[] mergeSort(Integer[] a) throws NullPointerException {
		if (a == null) {
			throw new NullPointerException();
		}
		return (mergeSort(a, 0, a.length - 1));
	}

	/*
	 * Sorts the input subarray of Integers a[p...q] using MergeSort and returns
	 * result. Sorting is stable.
	 */
	private static Integer[] mergeSort(Integer[] a, int p, int q) {
		if (p < q) {
			int middle = (p + q) / 2;
			Integer[] firstHalf = mergeSort(a, p, middle);
			Integer[] secondHalf = mergeSort(a, middle + 1, q);
			return merge(firstHalf, secondHalf);
		} else {
			return Arrays.copyOfRange(a, p, q + 1);
		}
	}

	/*
	 * Merges two sorted arrays of Integers into a single sorted array. Given two
	 * equal elements, one in a and one in b, the element in a precedes the element
	 * in b.
	 */
	private static Integer[] merge(Integer[] a, Integer[] b) {
		Integer[] result = new Integer[a.length + b.length];
		int aPointer = 0;
		int bPointer = 0;
		int resultPointer = 0;
		while (aPointer < a.length && bPointer < b.length) {
			if (a[aPointer] > b[bPointer]) {
				result[resultPointer++] = b[bPointer++];
			} else {
				result[resultPointer++] = a[aPointer++];
			}
		}
		while (aPointer < a.length) {
			result[resultPointer++] = a[aPointer++];
		}
		while (bPointer < b.length) {
			result[resultPointer++] = b[bPointer++];
		}
		return result;
	}
}