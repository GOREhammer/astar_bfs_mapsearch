package test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import mortar01.Searcher;

import org.junit.Test;

public class BFS_Test {
	
	public void setUp(){
		new File("testResults/results_wolfeb_178816527_178816525_1_bfs.txt").delete();
		new File("testResults/results_wolfeb_330767681_178817851_1_bfs.txt").delete();
		new File("testResults/results_wolfeb_330767681_178826160_1_bfs.txt").delete();
	}

	@Test
	public void testBFSTree() throws IOException {
		HashMap<String,String[]> fromTo = new HashMap<String,String[]>();
		
		fromTo.put("results_wolfeb_178816527_178816525_0_bfs.txt", new String[]{"178816527", "178816525"});
		fromTo.put("results_wolfeb_330767681_178817851_0_bfs.txt", new String[]{"330767681", "178817851"});
		fromTo.put("results_wolfeb_330767681_178826160_0_bfs.txt", new String[]{"330767681", "178826160"});
			
		for(String s : fromTo.keySet()){
			String config[] = {"FtWayneNoDups_nodes.txt", "FtWayneNoDups_links.txt", fromTo.get(s)[0], fromTo.get(s)[1],
				"testResults/"+s, "0", "bfs"};
			Searcher.main(config);
			BufferedReader testFile = new BufferedReader(new FileReader("testResults/"+s));
			BufferedReader correctFile = new BufferedReader(new FileReader("correctResults/"+s));
			String testLine = testFile.readLine();
			String correctLine = correctFile.readLine();
			while(true){
				if(correctLine == null || testLine == null)
					break;
				assertEquals(testLine, correctLine);
				testLine = testFile.readLine();
				correctLine = correctFile.readLine();
			}
			
			if(testLine == null && correctLine != testLine){
				fail("Size of files differ");
			}
			testFile.close();
			correctFile.close();
		}
	}
	
	@Test
	public void testBFSGraph() throws IOException {
		HashMap<String,String[]> fromTo = new HashMap<String,String[]>();
		setUp();
		
		fromTo.put("results_wolfeb_178816527_178816525_1_bfs.txt", new String[]{"178816527", "178816525"});
		fromTo.put("results_wolfeb_330767681_178817851_1_bfs.txt", new String[]{"330767681", "178817851"});
		fromTo.put("results_wolfeb_330767681_178826160_1_bfs.txt", new String[]{"330767681", "178826160"});
			
		for(String s : fromTo.keySet()){
			String config[] = {"FtWayneNoDups_nodes.txt", "FtWayneNoDups_links.txt", fromTo.get(s)[0], fromTo.get(s)[1],
				"testResults/"+s, "1", "bfs"};
			Searcher.main(config);
			BufferedReader testFile = new BufferedReader(new FileReader("testResults/"+s));
			BufferedReader correctFile = new BufferedReader(new FileReader("correctResults/"+s));
			String testLine = testFile.readLine();
			String correctLine = correctFile.readLine();
			while(true){
				if(correctLine == null || testLine == null)
					break;
				assertEquals(testLine, correctLine);
				testLine = testFile.readLine();
				correctLine = correctFile.readLine();
			}
			
			assert(correctLine == testLine);
			testFile.close();
			correctFile.close();
		}
	}

}
