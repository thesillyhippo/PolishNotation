/*
Name: Reducer.java
Execution: java prefixer {-r} file_name
Alternative Execution: java Reducer file_name
Dependencies: Queue.java, In.java, Reducer.java

This program is an extension of the prefixer program, although
this program can be run by itself (with dependencies). Reducer
has similar methods to prefixer, except that it reduces expressions
as they are added to the binary expression tree.
*/

public class Reducer
{
	//Linked Structure for Binary Expression Tree
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
	
	//makes a copy of TreeNode
	public TreeNode copyTree(TreeNode a)
	{
		if (a == null) return a;
		TreeNode b = new TreeNode();
		b.item = a.item;
		b.left = copyTree(a.left);
		b.right = copyTree(a.right);
		return b;
	}
	
	//returns operation of numbers based on input
	public double operation(String operator, double a, double b)
	{
		if(operator.equals("+")) return (double) a + b;
		else if (operator.equals("-")) return (double) a - b;
		else if (operator.equals("*")) return (double) a * b;
		else return (double) a / b;
 	}
	
	//categorizes type of token
	public String type(String a)
	{
		if (a.matches("(-)*[0-9]*(.)*[0-9]"))
		{
			return "number";
		}
		else if (a.matches("[a-zA-Z]"))
		{
			return "variable";
		}
		else if (a.equals("*"))
		{
			return "mult_op";
		}
		else if (a.equals("/"))
		{
			return "div_op";
		}
		else if (a.equals("+"))
		{
			return "add_op";
		}
		else if (a.equals("-"))
		{
			return "sub_op";
		}
		else return null;
	}

	//prints reduced prefix form
	public void printPrefix(TreeNode tree)
	{
		if (tree == null) return;
		
		String current_item = tree.item;
		if (current_item.matches("\\+|-|/|\\*"))
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
	
	
		//prints postfix
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

		//prints infix
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
	
	
	
	//determines if string s is the operator/operand expected
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
	
	//gets number/expression ready for binary expression tree
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
	
	//attaches sum/difference expression, calling appropriate reduce methods
	public TreeNode getSumDiff(Queue<String> tokenlist)
	{
		TreeNode a = getProdQuo(tokenlist);
		
		if (getToken(tokenlist, "+"))
		{
			TreeNode b = getSumDiff(tokenlist);
			TreeNode reduced_sum = reduceSum(a, b);
			return reduced_sum;
		}
		else if (getToken(tokenlist, "-"))
		{
			TreeNode b = getSumDiff(tokenlist);
			TreeNode reduced_diff = reduceDiff(a, b);
			return reduced_diff;
		}
		else
		{ 
			return a;
		}
	}
	
	//reduce method for when parent node is a multiplier operator
	public TreeNode reduceProduct(TreeNode x, TreeNode y)
	{
		TreeNode a = x;
		TreeNode b = y;
		String a_type = type(a.item);
		String b_type = type(b.item);
			
			//left node is a number
			if (a_type.equals("number"))
			{
				if (b_type.equals("number"))  
				{
					
					double product_result = operation("*", Double.parseDouble(a.item), 
					Double.parseDouble(b.item));
					
					String string_result = product_result + "";
					TreeNode result = new TreeNode(string_result, null, null);
					return result;
				}
				else if (b_type.equals("variable")) 
				{
					TreeNode result = new TreeNode("*", a, b);
					return result;
				}
				
				else if (b_type.equals("mult_op"))   
				{
					if (type(b.left.item).equals("number"))
					{
						TreeNode product_result = reduceProduct(a, b.left);
						b.left = product_result;
						return new TreeNode("*", b.left, b.right);
					}
					if (type(b.left.item).equals("variable")) 
					{
						if (type(b.right.item).equals("number"))
						{
							double product_result = operation("*", Double.parseDouble(a.item), 
							Double.parseDouble(b.right.item));
							
							a.item = product_result + "";
							b = b.left;
							TreeNode result = new TreeNode("*", a, b);
							return result;
						}
						
						else if (type(b.right.item).equals("variable"))
						{
							TreeNode result = new TreeNode("*", a, b);
							return result;
						}
						else if (type(b.right.item).equals("mult_op"))
						{
							TreeNode product_result = reduceProduct(a, b.right);
							b.right = product_result;
							return new TreeNode("*", b.left, b.right);
							
						}
						return new TreeNode("*", a, b);
						
					}
					else if (type(b.right.item).equals("variable"))  
					{
						if (type(b.left.item).equals("number")) 
						{
							double product_result = operation("*", Double.parseDouble(a.item), 
							Double.parseDouble(b.left.item));
							
							a.item = product_result + "";
							b = b.right;
							TreeNode result = new TreeNode("*", a, b);
							return result;
						}
						
						else if (type(b.left.item).equals("variable")) 
						{
							TreeNode result = new TreeNode("*", a, b);
							return result;
						}
					}
					
				}
				
				else if (b_type.equals("add_op"))
				{
					TreeNode product_left_reduced = reduceProduct(a, b.left);
					TreeNode product_right_reduced = reduceProduct(a, b.right);
					
					TreeNode result = new TreeNode("+", product_left_reduced, product_right_reduced);
					return result;	
				}
				else if (b_type.equals("sub_op"))
				{
					TreeNode product_left_reduced = reduceProduct(a, b.left);
					TreeNode product_right_reduced = reduceProduct(a, b.right);
					
					TreeNode result = new TreeNode("-", product_left_reduced, product_right_reduced);
					return result;
				}
			}
			
			else if (a_type.equals("variable"))
			{
				if (b_type.equals("number"))
				{
					TreeNode result = new TreeNode("*", a, b);
					return result;
				}
				else if (b_type.equals("variable"))
				{
					TreeNode result = new TreeNode("*", a, b);
					return result;
				}
				else if (b_type.equals("mult_op"))
				{
					TreeNode result = new TreeNode("*", a, b);
					return result;
				}
				else if (b_type.equals("add_op"))
				{
					
					TreeNode product_left = reduceProduct(a, b.left);
					TreeNode product_right = reduceProduct(a, b.right);
				
					TreeNode result = new TreeNode("+", product_left, product_right);
				
					return result;
				}
				else if (b_type.equals("sub_op"))
				{
					TreeNode product_left = reduceProduct(a, b.left);
					TreeNode product_right = reduceProduct(a, b.right);
				
					TreeNode result = new TreeNode("-", product_left, product_right);
				
					return result;
				}
			}
			
			else if (a_type.equals("mult_op"))
			{
				
				if (b_type.equals("number"))
				{
					TreeNode result = reduceProduct(b, a);
					return result;
					
				}
				else if (b_type.equals("variable"))
				{
					TreeNode result = reduceProduct(b, a);
					return result;
				}
				
				else if (b_type.equals("mult_op"))
				{
					if (type(a.left.item).equals("number"))
					{
						if (type(b.left.item).equals("number"))
						{
							
							double product = operation("*", Double.parseDouble(a.left.item), Double.parseDouble(b.left.item));
							b.left = a.right;
							a = new TreeNode(product + "", null, null);
							TreeNode result = new TreeNode("*", a, b);
							return reduceProduct(a, b);
						}
						else if (type(b.right.item).equals("number"))
						{
							double product = operation("*", Double.parseDouble(a.left.item), Double.parseDouble(b.right.item));
							b.right = a.right;
							a = new TreeNode(product + "", null, null);
							TreeNode result = new TreeNode("*", a, b);
							return reduceProduct(a, b);
						} 
						else if (type(b.left.item).equals("mult_op"))
						{
							TreeNode result = new TreeNode("*", a, b.left);
							return reduceProduct(a, b.left);
						}
						else if (type(b.right.item).equals("mult_op"))
						{
							TreeNode result = new TreeNode("*", a, b.right);
							return reduceProduct(a, b.right);
						}
						else return new TreeNode("*", a, b);
					}
					else if (type(a.right.item).equals("number"))
					{
						if (type(b.left.item).equals("number"))
						{
							
							double product = operation("*", Double.parseDouble(a.right.item), Double.parseDouble(b.left.item));
							b.left = a.left;
							a = new TreeNode(product + "", null, null);
							TreeNode result = new TreeNode("*", a, b);
							return reduceProduct(a, b);
						}
						else if (type(b.right.item).equals("number"))
						{
							double product = operation("*", Double.parseDouble(a.right.item), Double.parseDouble(b.right.item));
							b.right = a.left;
							a = new TreeNode(product + "", null, null);
							TreeNode result = new TreeNode("*", a, b);
							return reduceProduct(a, b);
						}
						else if (type(b.left.item).equals("mult_op"))
						{
							TreeNode result = new TreeNode("*", a, b.left);
							return reduceProduct(a, b.left);
						}
						else if (type(b.right.item).equals("mult_op"))
						{
							TreeNode result = new TreeNode("*", a, b.right);
							return reduceProduct(a, b.right);
						}
						else return new TreeNode("*", a, b);
					}
					else if (type(a.left.item).equals("mult_op"))
					{
						return reduceProduct(b, a);
					}
					else if (type(a.right.item).equals("mult_op"))
					{
						return reduceProduct(b, a);
					}
					
					
					
				}
				else if (b_type.equals("add_op"))
				{
					TreeNode a_copy = copyTree(a);
					b.left = reduceProduct(b.left, a);
					b.right = reduceProduct(b.right, a_copy);
					return new TreeNode("+", b.left, b.right);
				}
				
				else if (b_type.equals("sub_op"))
				{
					TreeNode a_copy = copyTree(a);
					b.left = reduceProduct(b.left, a);
					b.right = reduceProduct(b.right, a_copy);
					return new TreeNode("-", b.left, b.right);
				}
			}
			
			else if (a_type.equals("div_op"))
			{
				TreeNode first_product = reduceProduct(a.left, b);
				TreeNode second_quo = reduceQuo(first_product, a.right);
				return second_quo;
			}
			
			else if (a_type.equals("add_op"))
			{
				if (b_type.equals("number"))
				{
					return reduceProduct(b, a);
				}
				else if (b_type.equals("variable"))
				{
					return reduceProduct(b, a);
				}
				
				else if (b_type.equals("mult_op"))
				{
					return reduceProduct(b, a);
				}
				else if (b_type.equals("add_op"))  //FOIL
				{
					TreeNode a_left_copy = copyTree(a.left);
					TreeNode a_right_copy = copyTree(a.right);
					TreeNode b_left_copy = copyTree(b.left);
				
					TreeNode b_right_copy = copyTree(b.right);
					TreeNode first_node = reduceProduct(a.left, b.left);
					TreeNode outer_node = reduceProduct(a_left_copy, b.right);
					TreeNode inner_node = reduceProduct(a.right, b_left_copy);
				
				
					TreeNode last_node = reduceProduct(a_right_copy, b_right_copy);
					TreeNode result = new TreeNode("+", first_node, outer_node);
					result = new TreeNode("+", result, inner_node);
					result = new TreeNode("+", result, last_node);
					
					return result;
				}
				
				else if (b_type.equals("sub_op"))
				{
						TreeNode a_left_copy = copyTree(a.left);
						TreeNode a_right_copy = copyTree(a.right);
						TreeNode b_left_copy = copyTree(b.left);

						TreeNode b_right_copy = copyTree(b.right);
						TreeNode first_node = reduceProduct(a.left, b.left);
						TreeNode outer_node = reduceProduct(a_left_copy, b.right);
						TreeNode inner_node = reduceProduct(a.right, b_left_copy);


						TreeNode last_node = reduceProduct(a_right_copy, b_right_copy);
						TreeNode result = new TreeNode("-", first_node, outer_node);
						result = new TreeNode("+", result, inner_node);
						result = new TreeNode("-", result, last_node);

						return result;
				}
			}
		
			else if (a_type.equals("sub_op"))
			{
				if (b_type.equals("number"))
				{
					return reduceProduct(b, a);
				}
				else if (b_type.equals("variable"))
				{
					return reduceProduct(b, a);
				}
				else if (b_type.equals("mult_op"))
				{
					return reduceProduct(b, a);
				}
				else if (b_type.equals("add_op"))
				{
					TreeNode a_left_copy = copyTree(a.left);
					TreeNode a_right_copy = copyTree(a.right);
					TreeNode b_left_copy = copyTree(b.left);
				
					TreeNode b_right_copy = copyTree(b.right);
					TreeNode first_node = reduceProduct(a.left, b.left);
					TreeNode outer_node = reduceProduct(a_left_copy, b.right);
					TreeNode inner_node = reduceProduct(a.right, b_left_copy);
				
				
					TreeNode last_node = reduceProduct(a_right_copy, b_right_copy);
					TreeNode result = new TreeNode("+", first_node, outer_node);
					result = new TreeNode("-", result, inner_node);
					result = new TreeNode("-", result, last_node);
					
					return result;
				}
				else if (b_type.equals("sub_op"))
				{
					TreeNode a_left_copy = copyTree(a.left);
					TreeNode a_right_copy = copyTree(a.right);
					TreeNode b_left_copy = copyTree(b.left);
				
					TreeNode b_right_copy = copyTree(b.right);
					TreeNode first_node = reduceProduct(a.left, b.left);
					TreeNode outer_node = reduceProduct(a_left_copy, b.right);
					TreeNode inner_node = reduceProduct(a.right, b_left_copy);
				
				
					TreeNode last_node = reduceProduct(a_right_copy, b_right_copy);
					TreeNode result = new TreeNode("-", first_node, outer_node);
					result = new TreeNode("-", result, inner_node);
					result = new TreeNode("+", result, last_node);
					
					return result;
				}
			}
		return new TreeNode("*", a, b);	
	}
	
	//reduce method for when parent node is a divisor operator
	public TreeNode reduceQuo(TreeNode x, TreeNode y)
	{
		TreeNode a = x;
		TreeNode b = y;
		String a_type = type(a.item);
		String b_type = type(b.item);
		
		if (a_type.equals("number"))
		{
			if (b_type.equals("number"))
			{
				double quotient = operation("/", Double.parseDouble(a.item), Double.parseDouble(b.item));
				return new TreeNode(quotient + "", null, null);
			}
			
			if (b_type.equals("variable"))
			{
				return new TreeNode("/", a, b);
			}
			if (b_type.equals("mult_op"))
			{
				if (type(b.left.item).equals("number"))
				{
					double quotient = operation("/", Double.parseDouble(a.item), Double.parseDouble(b.left.item));
					TreeNode new_a = new TreeNode(quotient + "", null, null);
					return new TreeNode ("/", new_a, b.right);
				}
				else if (type(b.right.item).equals("number"))
				{
					double quotient = operation("/", Double.parseDouble(a.item), Double.parseDouble(b.right.item));
					TreeNode new_a = new TreeNode(quotient + "", null, null);
					return new TreeNode ("/", new_a, b.left);
				}
				else return new TreeNode("/", a, b);
			}
			if (b_type.equals("div_op"))
			{
				if (type(b.left.item).equals("number"))
				{
					double quotient = operation("/", Double.parseDouble(a.item), Double.parseDouble(b.left.item));
					TreeNode new_a = new TreeNode(quotient + "", null, null);
					return new TreeNode ("*", new_a, b.right);
				}
				
				else if (type(b.right.item).equals("number"))
				{
					double product = operation("*", Double.parseDouble(a.item), Double.parseDouble(b.right.item));
					TreeNode new_a = new TreeNode(product + "", null, null);
					return new TreeNode ("/", new_a, b.left);
				}
				else return new TreeNode("/", a, b);
			}
			if (b_type.equals("add_op"))
			{
				return new TreeNode("/", a, b);
			}
			if (b_type.equals("sub_op"))
			{
				return new TreeNode("/", a, b);
			}
			
		}
		
		else if (a_type.equals("variable"))
		{
			return new TreeNode("/", a, b);
		}
		
		else if (a_type.equals("mult_op"))
		{
			if (b_type.equals("number"))
			{
				if (type(a.left.item).equals("number"))
				{
					TreeNode a_left = reduceQuo(a.left, b);
					return new TreeNode("*", a_left, a.right);
				}
				
				else if (type(a.right.item).equals("number"))
				{
					TreeNode a_right = reduceQuo(a.right, b);
					return new TreeNode("*", a.left, a_right);
				}
				else if (type(a.left.item).equals("mult_op"))
				{
					a.left = reduceQuo(a.left, b);
					return a;
				}
				else if (type(a.left.item).equals("mult_op"))
				{
					a.right = reduceQuo(a.right, b);
					return a;
				}
				else if (type(a.left.item).equals("div_op"))
				{
					a.left = reduceQuo(a.left, b);
					return a;
				}
				else if (type(a.right.item).equals("mult_op"))
				{
					a.right = reduceQuo(a.right, b);
					return a;
				}
				else return new TreeNode("/", a, b);
			}
			else if (b_type.equals("div_op"))
			{
				return reduceProduct(a, b);
			}
			return new TreeNode("/", a, b);
			
		}
		
		else if (a_type.equals("div_op"))
		{
			if (b_type.equals("number"))
			{
				if (type(a.left.item).equals("number"))
				{
					TreeNode a_left = reduceQuo(a.left, b);
					return new TreeNode("/", a_left, a.right);
				}
				
				else if (type(a.right.item).equals("number"))
				{
					TreeNode a_right = reduceProduct(a.right, b);
					return new TreeNode("/", a.left, a_right);
				}
				
			}
		}
		
		else if (a_type.equals("add_op"))
		{
			TreeNode copy_b = copyTree(b);
			TreeNode a_left = reduceQuo(a.left, b);
		
			TreeNode a_right = reduceQuo(a.right, copy_b);
			TreeNode sum = new TreeNode("+", a_left, a_right);
			//***	printPrefix(sum); System.out.println("");
			
			return sum;
		}
		
		else if (a_type.equals("sub_op"))
		{
		 	TreeNode a_left = reduceQuo(a.left, b);
			TreeNode a_right = reduceQuo(a.right, b);
			return new TreeNode("-", a_left, a_right);
		}
		
		return new TreeNode("/", a, b);
	}
	
	//reduce method for when parent node is Sum operator
	public TreeNode reduceSum(TreeNode x, TreeNode y)
	{
		TreeNode a = x;
		TreeNode b = y;
		String a_type = type(a.item);
		String b_type = type(b.item);
		
		
		if (a_type.equals("number"))
		{
			if (b_type.equals("number"))
			{
			double sum = operation("+", Double.parseDouble(a.item), Double.parseDouble(b.item));
			return new TreeNode(sum + "", null, null);
			}
			
			else if (b_type.equals("variable"))
			{
				return new TreeNode("+", a, b);
			}
			
			else if (b_type.equals("mult_op"))
			{
				return new TreeNode("+", a, b);
			}
			
			else if (b_type.equals("add_op"))
			{
				if (type(b.left.item).equals("number"))
				{
					double sum = operation("+", Double.parseDouble(a.item), Double.parseDouble(b.left.item));
					b.left.item = sum + "";
					return new TreeNode("+", b.left, b.right);
				}
				if(type(b.right.item).equals("number"))
				{
					double sum = operation("+", Double.parseDouble(a.item), Double.parseDouble(b.right.item));
					b.right.item = sum + "";
					return new TreeNode("+", b.left, b.right);
				}
				if (type(b.left.item).equals("add_op"))
				{
					b.left = reduceSum(a, b.left);
					return new TreeNode("+", b.left, b.right);
				}
				if (type(b.right.item).equals("add_op"))
				{
					b.right = reduceSum(a, b.right);
					return new TreeNode("+", b.left, b.right);
				}
				else return new TreeNode("+", a, b);
			}
			else if (b_type.equals("sub_op"))
			{
				if (type(b.left.item).equals("number"))
				{
					double sum = operation("+", Double.parseDouble(a.item), Double.parseDouble(b.left.item));
					a.item = sum + "";
					return new TreeNode("-", a, b.right);
				}
				if (type(b.right.item).equals("number"))
				{
					double diff = operation("-", Double.parseDouble(a.item), Double.parseDouble(b.right.item));
					a.item = diff + "";
					return new TreeNode("+", a, b.left);
				}
				if (type(b.left.item).equals("add_op"))
				{
					TreeNode first_sum = reduceSum(a, b.left);
					TreeNode second_diff = reduceDiff(first_sum, b.right);
					return second_diff;
									
				}
				if (type(b.left.item).equals("sub_op"))
				{
					TreeNode first_sum = reduceSum(a, b.left);
					TreeNode second_diff = reduceDiff(first_sum, b.right);
					return second_diff;
				}
				if (type(b.right.item).equals("add_op"))
				{
					TreeNode first_sum = reduceSum(a, b.left);
					TreeNode second_diff = reduceDiff(first_sum, b.right);
					return second_diff;
				}
				if (type(b.right.item).equals("sub_op"))
				{
					TreeNode first_sum = reduceSum(a, b.left);
					TreeNode second_diff = reduceSum(first_sum, b.right);
					return second_diff;
				}
				TreeNode first_sum = reduceSum(a, b.left);
				TreeNode second_diff = reduceSum(first_sum, b.right);
				return second_diff;
			}
			else return new TreeNode("+", a, b);
			
		}
		
		else if (a_type.equals("variable"))
		{
			if (b_type.equals("number"))
			{
				return reduceSum(b, a);
			}
			else if (b_type.equals("variable"))
			{
				return new TreeNode("+", a, b);
			}
			else if (b_type.equals("mult_op"))
			{
				return new TreeNode("+", a, b);
			}
			else if (b_type.equals("add_op"))
			{
				return new TreeNode("+", a, b);
			}
			
		}
		
		else if (a_type.equals("mult_op"))
		{
			return new TreeNode("+", a, b);
			
		}
		else if (a_type.equals("div_op"))
		{
			if (b_type.equals("div_op"))
			{
				if (a.right.item.equals(b.right.item))
				{
					TreeNode copy_divisor = copyTree(b.right);
					TreeNode sum = reduceSum(a.left, b.left);
					
					TreeNode quotient = reduceQuo(sum, copy_divisor);
		
					
					return quotient;
				}
		
				else return new TreeNode("+", a, b);
			}
			else if (b_type.equals("add_op"))
			{
				
				TreeNode first_sum = reduceSum(a, b.left);
				TreeNode second_sum = reduceSum(first_sum, b.right);
					
				return second_sum;
			}
			else if (b_type.equals("sub_op"))
			{
				TreeNode first_sum = reduceSum(a, b.left);
				TreeNode second_sum = reduceDiff(first_sum, b.right);
				
				return second_sum;
			}
			return new TreeNode("+", a, b);
		}
	
		else if (a_type.equals("add_op"))
		{
			if (b_type.equals("number"))
			{
				return reduceSum(b, a);
			}
			else if (b_type.equals("variable"))
			{
				return reduceSum(b, a);
			}
			else if (b_type.equals("mult_op"))
			{
				return reduceSum(b, a);
			}
			else if (b_type.equals("div_op"))
			{ 
	
				return new TreeNode("+", a, b);
			
			}
			else if (b_type.equals("add_op"))
			{
				
				TreeNode a_first = reduceSum(a.left, b);
				TreeNode a_second = reduceSum(a.right, a_first);
				
				return a_second;
			}
			else if (b_type.equals("sub_op"))
			{
				TreeNode a_first = reduceSum(a, b.left);
				TreeNode a_second = reduceDiff(a_first, b.right);
				return a_second;
			}
		}
		
		else if (a_type.equals("sub_op"))
		{
			if (b_type.equals("sub_op"))
			{
				TreeNode a_first = reduceSum(a, b.left);
				TreeNode a_second = reduceDiff(a_first, b.right);
				return a_second;
			}
			else return reduceSum(b, a);
		}
		
		return new TreeNode("+", a, b);
	}
	
	//reduce method for when parent node is Difference operator
	public TreeNode reduceDiff(TreeNode x, TreeNode y)
	{
		TreeNode a = x;
		TreeNode b = y;
		String a_type = type(a.item);
		String b_type = type(b.item);
		
		if (a_type.equals("number"))
		{
			if (b_type.equals("number"))
			{
			double diff = operation("-", Double.parseDouble(a.item), Double.parseDouble(b.item));
			return new TreeNode(diff + "", null, null);
			}
			
			else if (b_type.equals("variable"))
			{
				return new TreeNode("-", a, b);
			}
			
			else if (b_type.equals("mult_op"))
			{
				return new TreeNode("-", a, b);
			}
			else if (b_type.equals("add_op"))
			{
				if (type(b.left.item).equals("number"))
				{
					double diff = operation("-", Double.parseDouble(a.item), Double.parseDouble(b.left.item));
					a.item = diff + "";
					return new TreeNode("-", a, b.right);
				}
				if (type(b.right.item).equals("number"))
				{
					double diff = operation("-", Double.parseDouble(a.item), Double.parseDouble(b.right.item));
					a.item = diff + "";
					return new TreeNode("-", a, b.left);
				}
				if (type(b.left.item).equals("add_op"))
				{
					b.left = reduceDiff(a, b.left);
					return new TreeNode("-", b.left, b.right);
				}
				if (type(b.right.item).equals("add_op"))
				{
					b.right = reduceDiff(a, b.right);
					return new TreeNode("-", b.left, b.right);
				}
				else return new TreeNode("-", a, b);
			}
		}
		
		else if (a_type.equals("variable"))
		{
		 return new TreeNode("-", a, b);	
		}
		
		else if (a_type.equals("mult_op"))
		{
			return new TreeNode("-", a, b);
		}
		else if (a_type.equals("div_op"))
		{
			if (b_type.equals("div_op"))
			{
				if (a.right.item.equals(b.right.item))
				{
					System.out.println("hi");
					TreeNode copy_divisor = copyTree(b.right);
					TreeNode diff = reduceDiff(a.left, b.left);
					
					TreeNode quotient = reduceQuo(diff, copy_divisor);
		
					
					return quotient;
				}
				else return new TreeNode("-", a, b);
			}
			else return new TreeNode("-", a, b);
		}
		else if (a_type.equals("add_op"))
		{
			if (b_type.equals("number"))
			{
				if (type(a.left.item).equals("number"))
				{
					a.left = reduceDiff(a.left, b);
					return new TreeNode("+", a.left, a.right);
				}
				if (type(a.right.item).equals("number"))
				{
					a.right = reduceDiff(a.right, b);
					return new TreeNode("+", a.left, a.right);
				}
				if (type(a.left.item).equals("add_op"))
				{
					a.left = reduceDiff(a.left, b);
					return new TreeNode("+", a.left, a.right);
				}
				if (type(a.right.item).equals("add_op"))
				{
					a.right = reduceDiff(a.right, b);
					return new TreeNode("+", a.left, a.right);
				}
				if (type(a.left.item).equals("sub_op"))
				{
					a.left = reduceDiff(a.left, b);
					return new TreeNode("+", a.left, a.right);
					
				}
				if (type(a.right.item).equals("sub_op"))
				{
					a.right = reduceDiff(a.right, b);
					return new TreeNode("+", a.left, a.right);
				}
			}
			if (b_type.equals("variable"))
			{
				return new TreeNode("-", a, b);
			}
			if (b_type.equals("mult_op"))
			{
				return new TreeNode("-", a, b);
			}
			if (b_type.equals("add_op"))
			{
				TreeNode first_diff = reduceDiff(a, b.left);
				TreeNode second_diff = reduceDiff(first_diff, b.right);
				return second_diff;
				
			}
			if (b_type.equals("sub_op"))
			{
				TreeNode first_diff = reduceDiff(a, b.left);
				TreeNode second_sum = reduceSum(first_diff, b.right);
				return second_sum;
			}
			return null;
	
		}
		else if (a_type.equals("sub_op"))
		{
			if (b_type.equals("number"))
			{		
				TreeNode new_a = new TreeNode("+", a.left, b);
				return reduceDiff(new_a, a.right);	
			}
			if (b_type.equals("variable"))
			{
				TreeNode new_a = new TreeNode("+", a.left, b);
				return reduceDiff(new_a, a.right);
			}
			if (b_type.equals("mult_op"))
			{
				TreeNode new_a = new TreeNode("+", a.left, b);
				return reduceDiff(new_a, a.right);
			}
			if (b_type.equals("add_op"))
			{
				TreeNode first_diff = reduceDiff(a, b.left);
				TreeNode second_diff = reduceDiff(first_diff, b.right);
				return second_diff;
			}
			if (b_type.equals("sub_op"))
			{
				TreeNode first_diff = reduceDiff(a, b.left);
				TreeNode second_sum = reduceSum(first_diff, b.right);
				return second_sum;
			}
		}
		
		return new TreeNode("-", a, b);
	}
	
	//adds product/quotient to binary expression tree
	public TreeNode getProdQuo(Queue<String> tokenlist)
	{
		
		TreeNode a = getNumber(tokenlist);
	
		
		if (getToken(tokenlist, "*"))
		{
		
			TreeNode b = getProdQuo(tokenlist);
			TreeNode reduced = reduceProduct(a, b);
			return reduced;
			
		}
		else if (getToken(tokenlist, "/"))
		{
			TreeNode b = getProdQuo(tokenlist);
			TreeNode reduced = reduceQuo(a, b);
			return reduced;
		}
		else
		{
			return a; }
	}
	
	//start method for reduced prefixer
	public void startReducer(Queue<String> tokenlist, boolean reduce)
	{
		TreeNode a = getSumDiff(tokenlist);
	
		System.out.print("Reduced Infix: ");
		printInfix(a);
		System.out.println();
		System.out.print("Reduced Postfix: ");
		printPostfix(a);
		System.out.println();
		System.out.print("Reduced Prefix: ");
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
}