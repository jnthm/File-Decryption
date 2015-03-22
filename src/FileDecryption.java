import java.io.*;
import java.util.*;

public class FileDecryption {
	
	private Map<Character, Integer> mCharFreq;
	private Map<Character, Integer> mSortedByFreq;
	
	public FileDecryption() {
		mCharFreq = new HashMap<Character, Integer>();
		mSortedByFreq = new LinkedHashMap<Character, Integer>();
	}
	
	public void getCharCount(String s) {
		for (Character c: s.toCharArray()) {
			if (!mCharFreq.containsKey(c)) {
				mCharFreq.put(c, 0);
			}
			mCharFreq.put(c, Integer.valueOf(mCharFreq.get(c) + 1));
		}
	}
	
	public void decipherChars(Map<Character, Integer> mSortedByFreq, Map<Character, Character> resultCharMap) {
		Iterator<Map.Entry<Character, Integer>> refIt = mSortedByFreq.entrySet().iterator();
		Iterator<Map.Entry<Character, Integer>> thisIt = this.mSortedByFreq.entrySet().iterator();
		
		while (refIt.hasNext() && thisIt.hasNext()) {
			Map.Entry<Character, Integer> currRefEntry = refIt.next();
			Map.Entry<Character, Integer> currThisEntry = thisIt.next();
			//System.out.println(currThisEntry.getKey() + "--->" +  currRefEntry.getKey());
			resultCharMap.put(currThisEntry.getKey(), currRefEntry.getKey());
		}
		
	}
 
	
	public void sortCharFreqMapByValues() {
		List<Map.Entry<Character, Integer>> toList = new LinkedList<Map.Entry<Character, Integer>>(mCharFreq.entrySet());
		System.out.println("list before sorting" + toList);	
		Collections.sort(toList, 
				new Comparator<Map.Entry<Character, Integer>> () {
					@Override
					public int compare(Map.Entry<Character, Integer> e1, Map.Entry<Character, Integer> e2) {
						return (e2.getValue().compareTo(e1.getValue()));
					}
				}
		);
		for (Map.Entry<Character, Integer> e : toList) {
			mSortedByFreq.put(e.getKey(), e.getValue());
		}
}
	
	public void writeDecrFile(String encrFileName, Map<Character, Character> resultCharMap) {
		String sCurrLine = null;
		BufferedReader br = null;
		Writer writer = null;
		
		try {
			br = new BufferedReader(new FileReader(encrFileName));
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.txt"), "utf-8"));
			while ((sCurrLine = br.readLine()) != null) {
				
				char[] currLineChars = new char[sCurrLine.length()];
				int i = 0;
				for (Character c: sCurrLine.toCharArray()) {
					//System.out.println(c + "--->" +  resultCharMap.get(c));
					currLineChars[i++] = resultCharMap.get(c);
				}
				writer.write(String.valueOf(currLineChars));
			}
		} catch (IOException e) {
			 e.printStackTrace();
		} finally {
			try {
			if (br != null) br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		
		String sCurrLine = null;
		BufferedReader br = null;
		FileDecryption fileIn = null, fileRef = null;
		
		/*
		 * Input file
		 */
		try {
			br = new BufferedReader(new FileReader("input.txt"));
			fileIn = new FileDecryption();
			while ((sCurrLine = br.readLine()) != null) {
				fileIn.getCharCount(sCurrLine);
			}
		} catch (IOException e) {
			 e.printStackTrace();
		} finally {
			try {
			if (br != null) br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		fileIn.sortCharFreqMapByValues();
		
		/*
		 * Reference file
		 */
		try {
			br = new BufferedReader(new FileReader("reference.txt"));
			fileRef = new FileDecryption();
			while ((sCurrLine = br.readLine()) != null) {
				fileRef.getCharCount(sCurrLine);
			}
		} catch (IOException e) {
			 e.printStackTrace();
		} finally {
			try {
			if (br != null) br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		fileRef.sortCharFreqMapByValues();
		
		Map<Character, Character> charMap = new HashMap<Character, Character>();
		fileIn.decipherChars(fileRef.mSortedByFreq, charMap);
		
		fileIn.writeDecrFile("input.txt", charMap);
		
		
	}
	
}