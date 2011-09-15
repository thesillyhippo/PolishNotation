/*
Name: PolishNotation.java
Execution: java PolishNotatio {-r} file_name
Dependencies: Queue.java, In.java, Reducer.java

This program converts an Infix expression to a Binary Expression Tree that can be used to print
the Infix, Prefix, or Postfix form. The program can also use the Reducer Class to reduce the expression.
The input is highly restrictive, requiring all operators/operands to be one
space from each other. The variable/number input allowed is in [a-zA-Z_0-9],
while the operator input is {"+", "-", "/", "*"}
*/

public class PolishNotation
{
	
 	public static TreeNode root;

	private class TreeNode
	{
		String item;
		boolean variable;
		TreeNode left, right;
			
		public TreeNode()
		{
			return;
		}
		
		public TreeNode(String item, TreeNode left, TreeNode right)
		{
			
			this.item = item;
			this.left = left;
			this.right = right;		
		}		
	}
	
	
	public void printPrefix(TreeNode tree)
	{
		if (tree == null) return;
		
		String current_item = tree.item;

		if  (current_item.matches("\\+|-|/|\\*"))
			{
				System.out.print("( ");
				System.out.print(tree.item + " ");
				printPrefix(tree.left);
				printPrefix(tree.right);
				System.out.print(") ");
			}
			
		else 
		{
			System.out.print(tree.item + " ");
			printPrefix(tree.left);
			printPrefix(tree.right);
		
		}
		
	}
	
	public void printPostfix(TreeNode tree)
	{
		if (tree == null) return;
		
		String current_item = tree.item;
		
		if (tree.left == null)
		{
			System.out.print(tree.item + " ");
			return;
		}
	
			
		if (tree.left != null && tree.right !=null)
			{
				System.out.print("( ");
				printPostfix(tree.left);
				printPostfix(tree.right);
				System.out.print(tree.item + " ");
				System.out.print(") ");
				
			}
			
		
	
		
	}
	
	
	public void printInfix(TreeNode tree)
	{
		if (tree == null) return;
		
		String current_item = tree.item;
		
		if (tree.left == null)
		{
			System.out.print(tree.item + " ");
		}
		
		
		if (tree.left !=null && tree.right !=null)
		{
			System.out.print("( ");
			printInfix(tree.left);
			System.out.print(tree.item + " ");
			printInfix(tree.right);
			System.out.print(") ");
		}
		
		
	}


	
	

	public static boolean getToken(Queue<String> tokenlist, String s)
	{
		if (tokenlist.peek() == null) return false;
		
		if (tokenlist.peek().equals(s))
		{
	
			String removed = tokenlist.dequeue();
			return true;
		}
		else return false;
	}
	
	public TreeNode getNumber(Queue<String> tokenlist)
	{
		if (getToken(tokenlist, "("))
		{
			TreeNode x = getSumDiff(tokenlist);
			boolean y = getToken(tokenlist, ")");
			return x;
		}
		else
		{
			
			String x = tokenlist.peek();
			if (!x.matches("[a-zA-Z_0-9]*")) return null;
			String removed = tokenlist.dequeue();
			TreeNode number = new TreeNode();
			number.item = removed;
			return number;
		}
	}
	
	public TreeNode getSumDiff(Queue<String> tokenlist)
	{
		TreeNode a = getProdQuo(tokenlist);
		if (getToken(tokenlist, "+"))
		{
			TreeNode b = getSumDiff(tokenlist);
			return new TreeNode("+", a, b);
		}
		else if (getToken(tokenlist, "-"))
		{
			TreeNode b = getSumDiff(tokenlist);
			return new TreeNode("-", a, b);
		}
		else
		{ 
			return a;
		}
	}
	
	public TreeNode getProdQuo(Queue<String> tokenlist)
	{
		
		TreeNode a = getNumber(tokenlist);
	
		if (getToken(tokenlist, "*"))
		{
		
			TreeNode b = getProdQuo(tokenlist);
			return new TreeNode("*", a, b);
			
		}
		else if (getToken(tokenlist, "/"))
		{
			TreeNode b = getProdQuo(tokenlist);
			return new TreeNode("/", a, b);
		}
		else
		{
			return a; }
	}
	
	public void start(Queue<String> tokenlist, boolean reduce)
	{
		TreeNode a = getSumDiff(tokenlist);
		System.out.print("Infix: ");
		printInfix(a);
		System.out.println();
		System.out.print("Postfix: ");
		printPostfix(a);
		System.out.println();
		System.out.print("Prefix: ");
		printPrefix(a);
		System.out.println();
		
	}
	
	public static void main(String[] args)
	{
	 	int args_length = args.length;
		boolean reduce = false;
		int file_numb = 0;
		if (args_length == 2)
		{
			for (int i = 0; i < 2; i++)
			{
				if (args[i].equals("-r"))
				{
					reduce = true;
					file_numb = 1 - i;
				}		
			}
		}
		
		if (reduce)
		{
			Reducer tree = new Reducer();
			In in = new In(args[file_numb]);
			Queue<String> tokenlist = new Queue<String>();

			while(!in.isEmpty())
			{
				String str = in.readString();
				tokenlist.enqueue(str);

			}
			tree.startReducer(tokenlist, reduce);
		}
		
		
		else 
		{
		PolishNotation tree = new PolishNotation();
		In in = new In(args[file_numb]);
		Queue<String> tokenlist = new Queue<String>();
	
		while(!in.isEmpty())
		{
			String str = in.readString();
			tokenlist.enqueue(str);
			
		}
		tree.start(tokenlist, reduce);
		}	
	}
}