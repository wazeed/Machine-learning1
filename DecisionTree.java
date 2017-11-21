import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class node {
	node leftChild;
	node rightChild;
	node parentNode;
	int leftAttr[];
	int rightAttr[];
	int value = -1;
	boolean leaf = false;
	int finAttr = -1;
}

public class DecisionTree {
	private static int count = 0;

	private static node AttrNode(node root, int[][] val, int[] flag, int attr,
			int[] indLis) {
		double gain = 0;
		int maxlInd[] = null;
		int maxrIndex[] = null;
		int max = -1;
		for (int i = 0; i < attr; i++) {
			if (flag[i] == 0) {
				double neg = 0, pos = 0;
				double leftChild = 0, rightChild = 0;
				double leftEntr = 0, rightEntr = 0;
				int[] leftChildIndex = new int[val.length];
				int[] rightChildIndex = new int[val.length];
				double Entr = 0;
				double rightChildpos = 0, rightChildneg = 0;
				double Gain = 0;
				double leftChildpos = 0, leftChildneg = 0;
				for (int k = 0; k < indLis.length; k++) {
					if (val[indLis[k]][attr] == 1) {
						pos++;
					} else {
						neg++;
					}
					if (val[indLis[k]][i] == 1) {
						rightChildIndex[(int) rightChild++] = indLis[k];
						if (val[indLis[k]][attr] == 1) {
							rightChildpos++;
						} else {
							rightChildneg++;
						}

					} else {
						leftChildIndex[(int) leftChild++] = indLis[k];
						if (val[indLis[k]][attr] == 1) {
							leftChildpos++;
						} else {
							leftChildneg++;
						}
					}
				}
				Entr = (-1 * (Math.log10(pos / indLis.length) / Math.log10(2))
						* (pos / indLis.length))
						+ (-1 * (Math.log10(neg / indLis.length) / Math.log10(2))
								* (neg / indLis.length));
				leftEntr = (-1 * (Math.log10(leftChildpos / (leftChildpos + leftChildneg)) / Math.log10(2))
						* (leftChildpos / (leftChildpos + leftChildneg)))
						+ (-1 * (Math.log10(leftChildneg / (leftChildpos + leftChildneg)) / Math.log10(2))
								* (leftChildneg / (leftChildpos + leftChildneg)));
				rightEntr = (-1 * (Math.log10(rightChildpos / (rightChildpos + rightChildneg)) / Math.log10(2))
						* (rightChildpos / (rightChildpos + rightChildneg)))
						+ (-1 * (Math.log10(rightChildneg / (rightChildpos + rightChildneg)) / Math.log10(2))
								* (rightChildneg / (rightChildpos + rightChildneg)));
				if (Double.compare(Double.NaN, Entr) == 0) {
					Entr = 0;
				}
				if (Double.compare(Double.NaN, leftEntr) == 0) {
					leftEntr = 0;
				}
				if (Double.compare(Double.NaN, rightEntr) == 0) {
					rightEntr = 0;
				}
				Gain = Entr - ((leftChild / (leftChild + rightChild) * leftEntr) + (rightChild / (leftChild + rightChild) * rightEntr));
				if (Gain >= gain) {
					gain = Gain;
					max = i;
					int leftChildTempArray[] = new int[(int) leftChild];
					for (int index = 0; index < leftChild; index++) {
						leftChildTempArray[index] = leftChildIndex[index];
					}
					int rightChildTempArray[] = new int[(int) rightChild];
					for (int index = 0; index < rightChild; index++) {
						rightChildTempArray[index] = rightChildIndex[index];
					}
					maxlInd = leftChildTempArray;
					maxrIndex = rightChildTempArray;
				}
			}
		}
		root.finAttr = max;
		root.leftAttr = maxlInd;
		root.rightAttr = maxrIndex;
		return root;
	}
	public static node decTreeRand(node root, int[][] val, int[] flag, int attr, int[] indLis,node parentNode) {
		if (root == null) 
		{
			root = new node();
			if (indLis == null || indLis.length == 0) {
				root.value = maxVal(root, val, attr);
				root.leaf = true;
				return root;
			}
			if (posValues(indLis, val, attr)) {
				root.value = 1;
				root.leaf = true;
				return root;
			}
			if (negValues(indLis, val, attr)) {
				root.value = 0;
				root.leaf = true;
				return root;
			}
			if (attr == 1 || attrIter(flag)) {
				root.value = maxVal(root, val, attr);
				root.leaf = true;
				return root;
			}
		}
		root = attrRand(root, val, flag, attr, indLis);
		root.parentNode = parentNode;
		if (root.finAttr != -1)
			flag[root.finAttr] = 1;
		int leftChildflag[] = new int[flag.length];
		int rightChildflag[] = new int[flag.length];
		for (int j = 0; j < flag.length; j++) 
		{
			leftChildflag[j] = flag[j];
			rightChildflag[j] = flag[j];
		}
		
		root.leftChild = decTreeRand(root.leftChild, val, leftChildflag, attr, root.leftAttr, root);
		root.rightChild = decTreeRand(root.rightChild, val, rightChildflag, attr, root.rightAttr, root);
		return root;
	}
	
	private static node attrRand(node root, int[][] val, int[] flag, int attr,int[] indLis) {
		int leftChildIndex[] = null;
		int rightChildIndex[] = null;
		
		List<Integer> attributes = new ArrayList<Integer>();
		
		for(int i=0;i<flag.length;i++)
		{
			if(flag[i]==0)
				attributes.add(i);
		}	
		
		Random generator = new Random();
		int randomIndex = generator.nextInt(attributes.size());
		int Index = attributes.get(randomIndex);

				double leftChild = 0;
				double rightChild = 0;
				int[] leftIndex = new int[val.length];
				int[] rightIndex = new int[val.length];
				for (int k = 0; k < indLis.length; k++) 
				{
					if (val[indLis[k]][Index] == 1) 
					{
						rightIndex[(int) rightChild++] = indLis[k];
					}
					else 
					{
						leftIndex[(int) leftChild++] = indLis[k];
					}
				}
				
					int leftChildTempArray[] = new int[(int) leftChild];
					for (int index = 0; index < leftChild; index++) {
						leftChildTempArray[index] = leftIndex[index];
					}
					int rightChildTempArray[] = new int[(int) rightChild];
					for (int index = 0; index < rightChild; index++) {
						rightChildTempArray[index] = rightIndex[index];
					}
					leftIndex = leftChildTempArray;
					rightIndex = rightChildTempArray;

		root.finAttr = Index;
		root.leftAttr = leftIndex;
		root.rightAttr = rightIndex;
		return root;
	}

	public static boolean posValues(int[] indLis, int[][] val, int attr) {
		boolean one = true;
		for (int i : indLis) {
			if (val[i][attr] == 0)
				one = false;
		}
		return one;
	}

	public static boolean negValues(int[] indLis, int[][] val, int attr) {
		boolean zero = true;
		for (int i : indLis) {
			if (val[i][attr] == 1)
				zero = false;
		}
		return zero;
	}

	public static int maxVal(node root, int[][] val, int attr) {
		int class1 = 0;
		int class0 = 0;
		if (root.parentNode == null) {
			int i = 0;
			for (i = 0; i < val.length; i++) {
				if (val[i][attr] == 1) {
					class1++;
				} else {
					class0++;
				}
			}
		} else {
			for (int i : root.parentNode.leftAttr) {
				if (val[i][attr] == 1) {
					class1++;
				} else {
					class0++;
				}
			}

			for (int i : root.parentNode.rightAttr) {
				if (val[i][attr] == 1) {
					class1++;
				} else {
					class0++;
				}
			}
		}
		return class0 > class1 ? 0 : 1;
	}

	public static boolean attrIter(int[] flag) {
		boolean allDone = true;
		for (int i : flag) {
			if (i == 0)
				allDone = false;
		}
		return allDone;
	}

	public static node decTreeId3(node root, int[][] val, int[] flag, int attr,
			int[] indLis, node parentNode) {
		if (root == null) {
			root = new node();
			if (indLis == null || indLis.length == 0) {
				root.value = maxVal(root, val, attr);
				root.leaf = true;
				return root;
			}
			if (posValues(indLis, val, attr)) {
				root.value = 1;
				root.leaf = true;
				return root;
			}
			if (negValues(indLis, val, attr)) {
				root.value = 0;
				root.leaf = true;
				return root;
			}
			if (attr == 1 || attrIter(flag)) {
				root.value = maxVal(root, val, attr);
				root.leaf = true;
				return root;
			}
		}
		root = AttrNode(root, val, flag, attr, indLis);
		root.parentNode = parentNode;
		if (root.finAttr != -1)
			flag[root.finAttr] = 1;
		int leftflag[] = new int[flag.length];
		int rightflag[] = new int[flag.length];
		for (int j = 0; j < flag.length; j++) {
			leftflag[j] = flag[j];
			rightflag[j] = flag[j];
		}

		root.leftChild = decTreeId3(root.leftChild, val, leftflag, attr, root.leftAttr, root);
		root.rightChild = decTreeId3(root.rightChild, val, rightflag, attr, root.rightAttr, root);
		return root;
	}

	
	public static node copyTree(node root) {
		if (root == null)
			return root;
		node temp = new node(); // creates temporary node
		temp.value = root.value;
		temp.leaf = root.leaf;
		temp.leftAttr = root.leftAttr;
		temp.rightAttr = root.rightAttr;
		temp.finAttr = root.finAttr;
		temp.parentNode = root.parentNode;
		temp.leftChild = copyTree(root.leftChild);
		temp.rightChild = copyTree(root.rightChild);
		return temp;
	}


	private static int classifier(int[] setval, node newRoot) {
		int index = newRoot.finAttr;
		int classifier = 0;
		node testRoot = newRoot;
		while (testRoot.value == -1) {
			if (setval[index] == 1) {
				testRoot = testRoot.rightChild;
			} else {
				testRoot = testRoot.leftChild;
			}
			if (testRoot.value == 1 || testRoot.value == 0) {
				if (setval[setval.length - 1] == testRoot.value) {
					classifier = 1;
					break;
				} else {
					break;
				}
			}
			index = testRoot.finAttr;
		}
		return classifier;
	}

	private static int maxAtGiveNode(node root, int[][] val, int attr) {
		int class1 = 0;
		int class0 = 0;
		if (root.leftAttr != null) {
			for (int i : root.leftAttr) {
				if (val[i][attr] == 1) {
					class1++;
				} else {
					class0++;
				}
			}
		}

		if (root.rightAttr != null) {
			for (int i : root.rightAttr) {
				if (val[i][attr] == 1) {
					class1++;
				} else {
					class0++;
				}
			}
		}
		return class0 > class1 ? 0 : 1;
	}

	private static void actValue(String path, int[][] val, String[] attrHeaders, int[] flag,
			int[] indLis, int attr) {
		String File = path;
		BufferedReader bufferedReader = null;
		String line = "";
		String splitOn = ",";
		for (int k = 0; k < attr; k++) {
			flag[k] = 0;
		}
		int k = 0;
		for (k = 0; k < val.length; k++) {
			indLis[k] = k;
		}
		try {

			bufferedReader = new BufferedReader(new FileReader(File));
			int i = 0;
			while ((line = bufferedReader.readLine()) != null) {
				String[] params = line.split(splitOn);
				int j = 0;
				if (i == 0) {
					for (String param : params) {
						attrHeaders[j++] = param;
					}
				}

				else {

					for (String param : params) {
						val[i][j++] = Integer.parseInt(param);
					}
				}
				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
		 private static int noOfNodes(node root)
		    {
		        if (root == null)
		            return 0;
		        else
		            return(noOfNodes(root.leftChild) + 1 + noOfNodes(root.rightChild));
		    }
	private static int depthSum( node node, int depth ) {
   if ( node == null ) {
      return 0;
   }
   else if ( node.leftChild == null && node.rightChild == null) {
      return depth;
   }
   else {
      return depthSum(node.leftChild, depth + 1) 
                  + depthSum(node.rightChild, depth + 1);
   }
} 
	 
	
	private static int leafNodeCount(node root)
	{
	        if (root == null) {
	            return 0;
	        }
	        if (root.leftChild == null && root.rightChild == null) {
	            return 1;
	        } else {
	            return leafNodeCount(root.leftChild) + leafNodeCount(root.rightChild);
	        }
	}

	public static void main(String[] args) {

		int[] rwColn = rowsColumns(args[0]);
		int[][] val = new int[rwColn[1]][rwColn[0]];
		String[] attrHeaders = new String[rwColn[0]];
		int[] flag = new int[rwColn[0]];
		int[] indLis = new int[val.length];
		actValue(args[0], val, attrHeaders, flag, indLis, rwColn[0]);
		node root1 = decTreeId3(null, val, flag, rwColn[0] - 1, indLis, null);
		node root2 = decTreeRand(null, val, flag, rwColn[0] - 1, indLis, null);
		treeOut(root1, 0, attrHeaders);
		System.out.println("-----------");
		treeOut(root2, 0, attrHeaders);
		System.out.println("-----------");


		System.out.println("The Accuracy over Testing data for decision Tree using ID3 " + accuracy(args[2], root1)*100+"%");
		System.out.println("The Accuracy over Testing data for decision Tree using Random Numbers "+ accuracy(args[2], root2)*100+"%");

		System.out.println("Number of nodes in decision Tree by ID3 "+ noOfNodes(root1));
       	 	System.out.println("Number of nodes in decision Tree by random  "+ noOfNodes(root2));

		double tree1Depth = (double)depthSum(root1,1)/leafNodeCount(root1);
        	System.out.println("The average depth of decision Tree ID3 " + tree1Depth );
       		double tree2Depth = (double)depthSum(root2,1)/leafNodeCount(root2);
        	System.out.println("The average depth of decision Tree random " + tree2Depth );
	}

	private static int[] rowsColumns(String File) {
		BufferedReader bufferedReader = null;
		String line = "";
		String splitOn = ",";
		int count = 0;
		int[] rwColns = new int[2];
		try {

			bufferedReader = new BufferedReader(new FileReader(File));
			while ((line = bufferedReader.readLine()) != null) {
				if (count == 0) {
					String[] colns = line.split(splitOn);
					rwColns[0] = colns.length;
				}
				count++;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		rwColns[1] = count;
		return rwColns;
	}



	private static double accuracy(String path, node root) {
		double accuracy = 0;
		int[] rwColns = rowsColumns(path);
		String File = path;
		int[][] testSet = new int[rwColns[1]][rwColns[0]];
		BufferedReader bufferReader = null;
		String line = "";
		String splitOn = ",";
		try {

			bufferReader = new BufferedReader(new FileReader(File));
			int i = 0;
			int count = 0;
			while ((line = bufferReader.readLine()) != null) {
				String[] params = line.split(splitOn);
				int j = 0;
				if (count == 0) {
					count++;
					continue;
				}

				else {

					for (String param : params) {
						testSet[i][j++] = Integer.parseInt(param);
					}
				}
				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferReader != null) {
				try {
					bufferReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		int[][] testData = testSet;
		for (int i = 0; i < testData.length; i++) {
			accuracy += classifier(testData[i], root);
		}
		return accuracy / testData.length;
	}

	private static void treeOut(node root, int lines, String[] attrHeaders) {
		int lineLoop = lines;
		if (root.leaf) {
			System.out.println(" " + root.value);
			return;
		}
		for (int i = 0; i < lineLoop; i++) {
			System.out.print("| ");
		}
		if (root.leftChild != null && root.leftChild.leaf && root.finAttr != -1)
			System.out.print(attrHeaders[root.finAttr] + "= 0 :");
		else if (root.finAttr != -1)
			System.out.println(attrHeaders[root.finAttr] + "= 0 :");

		lines++;
		treeOut(root.leftChild, lines, attrHeaders);
		for (int i = 0; i < lineLoop; i++) {
			System.out.print("| ");
		}
		if (root.rightChild != null && root.rightChild.leaf && root.finAttr != -1)
			System.out.print(attrHeaders[root.finAttr] + "= 1 :");
		else if (root.finAttr != -1)
			System.out.println(attrHeaders[root.finAttr] + "= 1 :");
		treeOut(root.rightChild, lines, attrHeaders);
	}
}

