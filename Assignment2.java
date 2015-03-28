package assignment2;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

class MainFrame extends JFrame{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	JLabel LogoLabel;
	JLabel headLabel;
	JButton notifyButton;
	LoginPanel loginPanel;
	SignupPanel signUpPanel;
	SqlPanel sqlPanel;
	ButtonPanel buttonPanel;
//	PostandSearchPanel postandsearch;
	ResultPanel resultPanel;
	Connection conn=null;
	ArrayList<String> requester = new ArrayList<String>();
	ArrayList<String> Relation= new ArrayList<String>();
	int countrequest=0;
	JTextArea resultArea = null;
	JScrollPane scrollPane = null;
	int trigger = 0;
	JLabel showLabel;
	int hasRequest = 0;
	StringBuffer SQLOut = new StringBuffer ();

	MainFrame(){
		setResizable(false);
		setLayout(null);
		setSize(1100, 700);
		int width=Toolkit.getDefaultToolkit().getScreenSize().width;
		int height=Toolkit.getDefaultToolkit().getScreenSize().height;
		setLocation((width-1100)/2,(height-700)/2);
		setTitle("This is GUI for database homework");
		SetLogo();
		setLoginPanel();
		setSignupPanel();
		setSqlPanel();
		setButtonPanel();
		setResultPanel();
	//	postandsearch.disablePanel();
		buttonPanel.disablePanel();
	}

	public void disableResult(){
    	resultArea.setText("");
    	resultArea.setEditable(false);
    	resultArea.setEnabled(false);
    	scrollPane.setEnabled(false);
	}

	public void SetLogo(){
	Image image;
	try {
		image = ImageIO.read(new File("usc_viterbi_logo.jpg"));
		ImageIcon icon = new ImageIcon(image);
		LogoLabel = new JLabel();
		LogoLabel.setIcon(icon);
		LogoLabel.setBounds(830,500,300,150);

		add(LogoLabel);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  //this generates an image file

	}

	

	public void setButtonPanel(){
		buttonPanel = new ButtonPanel();
		buttonPanel.setBounds(30, 380, 700, 90);
		this.add(buttonPanel);

		//Your Account
		buttonPanel.buttons[0].addActionListener(new ActionListener() {
	          
            public void actionPerformed(ActionEvent e) {
            	StringBuffer result= new StringBuffer();
            	/*Fill this function*/
            	/*Press this your account button, you should be able to list*/
            	/* current log in customer information in the result panel (Including Email, First Name, Last Name, Address)*/
            	/*You can define the output format*/
            	conn=ConnectDB.openConnection();
            	//find info
            	String QueryStrC = "SELECT * FROM Customers WHERE Email='"+loginPanel.username.getText()+"'";
              	String QueryStrS = "SELECT * FROM Sellers WHERE Email='"+loginPanel.username.getText()+"'";
              	SQLOut.append(QueryStrC+"\n\n");
              	SQLOut.append(QueryStrS+"\n\n");
              	
              	Statement stmt;
              	Statement stmt1;
				try {
					stmt = conn.createStatement();
					stmt1 = conn.createStatement();
					ResultSet reC = stmt.executeQuery(QueryStrC);
					ResultSet reS = stmt1.executeQuery(QueryStrS);
					if(reC.next())
					{
						String email = reC.getString("Email");
						String fName = reC.getString("FName");
						String lName = reC.getString("LName");
						String addressID = reC.getString("AddressID");
						String address = null;
						String QueryAddr = "SELECT * FROM Address WHERE AddrID='"+addressID+"'";
						SQLOut.append(QueryAddr+"\n\n");
						ResultSet addr = stmt.executeQuery(QueryAddr);
						if(addr.next())
							address = addr.getString("AptNum") + addr.getString("Street_Address") + addr.getString("City") + addr.getString("State") + addr.getString("Zipcode");
						result.append("Email: " + email + "\n" + "First Name: " + fName + "\n" + "Last Name: " + lName + "\n" + "Address: " + address);
					}
					else if(reS.next())
					{
						String email = reS.getString("Email");
						String fName = reS.getString("FName");
						String lName = reS.getString("LName");
						String addressID = reS.getString("AddressID");
						String address = null;
						String QueryAddr = "SELECT * FROM Address WHERE AddrID='"+addressID+"'";
						SQLOut.append(QueryAddr+"\n\n");
						ResultSet addr = stmt.executeQuery(QueryAddr);
						if(addr.next())
							address = addr.getString("AptNum") + addr.getString("Street_Address") + addr.getString("City") + addr.getString("State") + addr.getString("Zipcode");
						result.append("Email: " + email + "\n" + "First Name: " + fName + "\n" + "Last Name: " + lName + "\n" + "Address: " + address);
					}
					
					ConnectDB.closeConnection(conn);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}           	
				setSQLOutput(SQLOut); 
				resultArea.setText(result.toString());
            }
        });
		//All Products
		buttonPanel.buttons[1].addActionListener(new ActionListener() {
          
            public void actionPerformed(ActionEvent e) {
            	//StringBuffer result= new StringBuffer();
            	/*Fill this function*/
            	/*Press this all products button, you should be able to list all the products which are visible to you*/
            	/*You can define the output format*/
            	conn=ConnectDB.openConnection();
            	//find info
            	String QueryStr = "SELECT * FROM Products";    
            	SQLOut.append(QueryStr+"\n\n");
          	
              	Statement stmt;
				try {
					stmt = conn.createStatement();
					ResultSet re = stmt.executeQuery(QueryStr);
					resultArea.setText("");
					while(re.next())
					{
						String ID = re.getString("ProductID");
						String category = re.getString("ProductCategory");
						String brand = re.getString("Brand");
						String name = re.getString("ProductName");
						String price = String.valueOf(re.getInt("Price"));
						resultArea.append(ID + category + brand + name + price +"\n");
					}					
					
					ConnectDB.closeConnection(conn);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}           	
				setSQLOutput(SQLOut); 

            }
        });
		//Choose Category
		buttonPanel.buttons[2].addActionListener(new ActionListener() {
          
            public void actionPerformed(ActionEvent e) {
            	final Frame0 frame=new Frame0();
                frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                frame.setVisible(true);
                resultArea.setText("");
                frame.btn1.addActionListener(new ActionListener() {
                 
                    public void actionPerformed(ActionEvent e) {
                    	/*Fill this function*/
    	            	/*Press this choose category Button, after choosing and pressing OK*/
    	            	/* you should be able to list all products belong to this category*/
                    	String type = (String)frame.combo.getSelectedItem();
                    	type = type.replace("&"," ");
                    	conn=ConnectDB.openConnection();
                    	//find info
                    	String QueryStr = "SELECT * FROM Products WHERE ProductCategory = '" + type + "'";             
                    	SQLOut.append(QueryStr+"\n\n");
                    	
                      	Statement stmt;
        				try {
        					stmt = conn.createStatement();
        					ResultSet re = stmt.executeQuery(QueryStr);
        					resultArea.setText("");
        					while(re.next())
        					{
        						String ID = re.getString("ProductID");
        						String category = re.getString("ProductCategory");
        						String brand = re.getString("Brand");
        						String name = re.getString("ProductName");
        						String price = String.valueOf(re.getInt("Price"));
        						resultArea.append(ID + category + brand + name + price +"\n");
        					}
        					
        					
        					ConnectDB.closeConnection(conn);
        				} catch (SQLException e1) {
        					// TODO Auto-generated catch block
        					e1.printStackTrace();
        				}           	
        				setSQLOutput(SQLOut);
                    	
                    }
                });
            }
        });
		//Set Price Range
		buttonPanel.buttons[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	        	final Frame1 frame=new Frame1("Please input Price Range ");
	            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	            frame.setVisible(true);

	            frame.btn1.addActionListener(new ActionListener() {
	               
	                public void actionPerformed(ActionEvent e) {
	                	/*Fill this function*/
	                	/*Press this set price range Button, you should be able to set price range*/
	                	/*Pressing "Set Price Range" button, a new window will pop out. */
	                	/*Then you can enter "Min_Price" & "Max_Price" and press "Search" button, */
	                	/*and then all products belong to category that you choose should be shown in the result panel.*/
	                	String buf = frame.txtfield[0].getText();
	                	Integer min = Integer.parseInt(buf);
	                	buf = frame.txtfield[1].getText();
	                	Integer max = Integer.parseInt(buf);
	                	conn=ConnectDB.openConnection();
                    	//find info
                    	String QueryStr = "SELECT * FROM Products WHERE Price > " + min + "AND Price < " + max;             
                    	SQLOut.append(QueryStr+"\n\n");
                    	
                      	Statement stmt;
        				try {
        					stmt = conn.createStatement();
        					ResultSet re = stmt.executeQuery(QueryStr);
        					resultArea.setText("");
        					while(re.next()) 
        					{
        						String ID = re.getString("ProductID");
        						String category = re.getString("ProductCategory");
        						String brand = re.getString("Brand");
        						String name = re.getString("ProductName");
        						String price = String.valueOf(re.getInt("Price"));
        						resultArea.append(ID + category + brand + name + price +"\n");
        					}
        					
        					
        					ConnectDB.closeConnection(conn);
        				} catch (SQLException e1) {
        					// TODO Auto-generated catch block
        					e1.printStackTrace();
        				}           	
        				setSQLOutput(SQLOut);
	                }
	            });

	        }
			});
        //Order Products
		buttonPanel.buttons[4].addActionListener(new ActionListener() {
           
        public void actionPerformed(ActionEvent e) {
        	final Frame2 frame=new Frame2();
            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            frame.setVisible(true);

            frame.btn1.addActionListener(new ActionListener() {
               
                public void actionPerformed(ActionEvent e) {
                	/*Fill this function*/     
//                	Press "Order Products" button, a new window will pop out. 
//                	Then you can enter "Product ID" and "Quantity", and then you press continue, 
//                	the Total Price should be shown correctly. 
//                	Then, you can press "Place Order" to complete this order. 
//                	This new order should be synchronized in the database.
                	String productID = frame.productID.getText();
                	String quan = frame.quantity.getText();
                	Integer quantity = Integer.parseInt(quan);

                	conn=ConnectDB.openConnection();
                	//find info
                	String QueryStr = "SELECT * FROM Products WHERE ProductID = '" + productID + "'";             
                	SQLOut.append(QueryStr+"\n\n");
                	
                  	Statement stmt;
    				try {
    					stmt = conn.createStatement();
    					ResultSet re = stmt.executeQuery(QueryStr);
    					resultArea.setText("");
    					while(re.next()) 
    					{   						
    						String buf = String.valueOf(re.getInt("Price"));
    						Integer price = Integer.parseInt(buf);
    						Integer totalPrice = price * quantity;
    						frame.totalPrice.setText(totalPrice.toString());  		
    					}
    					
    					
    					ConnectDB.closeConnection(conn);
    				} catch (SQLException e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				}           	
    				setSQLOutput(SQLOut);
                }
            });
            frame.btn2.addActionListener(new ActionListener() {
           
                public void actionPerformed(ActionEvent e) {
                	/*Fill this function*/
                	/*Press this accept all Button, you should be able to accept all friend request and add this information into friend relationship table*/
                	/*pop up a standard dialog box to show <succeed or failed>*/
                	String quan = frame.quantity.getText();
                	Integer quantity = Integer.parseInt(quan);
                	
                	String productID = frame.productID.getText();
                	String customer = loginPanel.username.getText();
                	String total = frame.totalPrice.getText();
                	Integer totalPrice = Integer.parseInt(total);
                	
                	Calendar c = Calendar.getInstance();
                	int year, month, day = 0;
                	year = c.get(Calendar.YEAR);
                	month = c.get(Calendar.MONTH)+1;
                	day = c.get(Calendar.DAY_OF_MONTH);
                	String place = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
                	//+1
                	String shipped = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day+1);                	
                	//+4
                	String esti = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day+4);                	
                	//+5
                	String signed = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day+5);
                    String track = null;
                	Integer trackingNumb = 0;
                	String trackingNum = null;
                	Integer stockQ = 0;
                	String sQ = null;
                	String quantityID = null;
                	Integer quID = 0;
                	String OrderID = null;
                	Integer OID = 0;
                	
                    resultArea.setText("");
                	conn=ConnectDB.openConnection();
                	
                	String queryTracking = "SELECT TrackingNum FROM Orders WHERE TrackingNum = (SELECT MAX(TrackingNum) FROM Orders)";               	
                	String stockQuantity = "SELECT StockQuantity FROM Products WHERE ProductID = '" + productID + "'";
                	String queryOID = "SELECT * FROM Orders WHERE TrackingNum = (SELECT MAX(TrackingNum) FROM Orders)";
                	String queryQID = "SELECT * FROM Quantity WHERE TrackingNum = (SELECT MAX(TrackingNum) FROM Quantity)";
                	  
                	SQLOut.append(queryTracking+"\n"+stockQuantity+"\n"+queryOID+queryQID+"\n\n");
                	
                  	Statement stmt;
    				try {
    					stmt = conn.createStatement();
    					ResultSet tracking = stmt.executeQuery(queryTracking);     	    					
    					if(tracking.next())
    						track = tracking.getString("TrackingNum");    					
    					//resultArea.append(track);
    					ResultSet or = stmt.executeQuery(queryOID);
    					while(or.next())
    						OrderID = or.getString("OrderID");
    					ResultSet qu = stmt.executeQuery(queryQID);
    					while(qu.next())
    						quantityID = qu.getString("QuanID");
    					    					    					
    					ResultSet stock = stmt.executeQuery(stockQuantity);			
    					if(stock.next())
    					{
    						stockQ = Integer.parseInt(stock.getString("StockQuantity"));
    						if(stockQ < quantity)
    							JOptionPane.showMessageDialog(null, "No enough products!");
    						else
    						{
    							trackingNumb = Integer.parseInt(track.trim());
    							trackingNumb++;
    							trackingNum = trackingNumb.toString();
    							//
    							OrderID = OrderID.replace("O", "");
    							OID = Integer.parseInt(OrderID.trim());
    							OID++;
    							OrderID = "O" + OID;
    							//
    							quantityID = quantityID.replace("Q", "");
    							quID = Integer.parseInt(quantityID.trim());
    							quID++;
    							quantityID = "Q" + quID;
    							//
    							stockQ = stockQ -  quantity;
    	    					
    	    					//resultArea.append(trackingNum);
    	    					//resultArea.append(OrderID + quantityID+sQ);
    	    					
    	    					//insert into orders
    	    					//String insertOrders = "INSERT INTO Orders VALUES('"+OrderID+"','"+loginPanel.username.getText()+"',"+totalPrice+",to_date('"+place+"','YY-MM-DD')"+",to_date('"+shipped+"','YY-MM-DD')"+",to_date('"+esti+"','YY-MM-DD')"+",to_date('"+signed+"','YY-MM-DD'),'"+trackingNum+"')";
    							String insertOrders = "INSERT INTO Orders VALUES('"+OrderID+"','"+loginPanel.username.getText()+"',"+totalPrice+",to_date('"+place+"','YY-MM-DD'),to_date('"+place+"','YY-MM-DD'),to_date('"+place+"','YY-MM-DD'),to_date('"+place+"','YY-MM-DD'),'"+trackingNum+"')";
    	    					System.out.println(insertOrders);
    	    					stmt.executeQuery(insertOrders);
    	    					
    	    					//insert into quantity
    	    					String insertQuan = "INSERT INTO Quantity VALUES('"+quantityID+"','"+loginPanel.username.getText()+"','"+trackingNum+"','"+productID+"',"+quantity+")";
    	    					System.out.println(insertQuan);
    	    					stmt.executeQuery(insertQuan);
    	    					
    	    					//update stockquantity of products
    	    					String update = "UPDATE Products SET StockQuantity ="+stockQ+"WHERE ProductID ='"+productID+"'";
    	    					System.out.println(update);
    	    					stmt.executeQuery(update);
    	    					SQLOut.append(insertOrders+"\n"+insertQuan+"\n"+update+"\n");
    						}
    					}
    					
    					
    					ConnectDB.closeConnection(conn);
    						
    					
    				} catch (SQLException e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				}        
    				setSQLOutput(SQLOut);
                }
            });

        }
		});
		//Your Orders
		buttonPanel.buttons[5].addActionListener(new ActionListener() {
	          
            public void actionPerformed(ActionEvent e) {
            	StringBuffer result= new StringBuffer();
            	/*Fill this function*/
            	/*Press "Your Orders", all order history of this customer should be shown in the result panel.*/
            	String queryOrder = "SELECT * FROM Orders O WHERE Customer ='"+loginPanel.username.getText()+"'";
            	conn=ConnectDB.openConnection();


              	SQLOut.append(queryOrder+"\n\n");
              	resultArea.setText("");
              	Statement stmt;
				try {
					stmt = conn.createStatement();

					ResultSet re = stmt.executeQuery(queryOrder);
					while(re.next())
					{
						for(int i = 1; i<9;i++)
							resultArea.append(re.getString(i));
					}				
					
					ConnectDB.closeConnection(conn);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}           	
				setSQLOutput(SQLOut); 

            }
        });
		//Review Products
		buttonPanel.buttons[6].addActionListener(new ActionListener() {
	           
	        public void actionPerformed(ActionEvent e) {
	        	final Frame5 frame=new Frame5("Product ID : ","Review : ");
	            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	            frame.setVisible(true);

	            frame.btn1.addActionListener(new ActionListener() {
	               
	                public void actionPerformed(ActionEvent e) {
	                	/*Fill this function*/
	                	/*Press "Review Products" button, a new window will pop out. */
	                	/*Input product ID and review content and press the OK button, this information should be inserted into database.*/
	                	String ReviewID = null;
	                	Integer review = 0;
	                	
	                	String queryRID = "SELECT * FROM Reviews";
	                	
	                						
						conn=ConnectDB.openConnection();


		              	SQLOut.append(queryRID+"\n\n");
		              	resultArea.setText("");
		              	Statement stmt;
						try {
							stmt = conn.createStatement();
							ResultSet r = stmt.executeQuery(queryRID);
	    					while(r.next())
	    						ReviewID = r.getString("ReviewID");
	    					ReviewID = ReviewID.replace("R", "");
	    					review = Integer.parseInt(ReviewID.trim());
	    					review++;
	    					ReviewID = "O" + review;
	    					
	    					Calendar cal = Calendar.getInstance();
	    					int y = cal.get(Calendar.YEAR);
	    					int m = cal.get(Calendar.MONTH);
	    					int d = cal.get(Calendar.DAY_OF_MONTH);
	    					String date = y+"-"+m+"-"+d;
	    					
	    					String customer = loginPanel.username.getText();
	    					String product = frame.txtfield.getText();
	    					String content = frame.textArea.getText();
	    					
	    					String insert = "INSERT INTO Reviews VALUES('"+ReviewID+"',to_date('"+date+"','YY-MM-DD'),"+0+",'"+content+"','"+customer+"','"+product+"')";
							stmt.executeQuery(insert);
							SQLOut.append(insert);
							
							ConnectDB.closeConnection(conn);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}           	
						setSQLOutput(SQLOut); 
	                }
	            });


	        }
			});
		//List All Reviews
		buttonPanel.buttons[7].addActionListener(new ActionListener() {
	           
	        public void actionPerformed(ActionEvent e) {
	        	final Frame4 frame=new Frame4("Product ID : ","Submit");
	            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	            frame.setVisible(true);

	            frame.btn1.addActionListener(new ActionListener() {
	               
	                public void actionPerformed(ActionEvent e) {
	                	/*Fill this function*/
	                	/*Press "List All Reviews" button, a new window will pop out. */
	                	/*Input "Product ID" and press submit, all reviews about this product should be shown in the result panel.*/
	                						
						conn=ConnectDB.openConnection();

		              	resultArea.setText("");
		              	Statement stmt;
						try {
							stmt = conn.createStatement();

	    					String product = frame.txtfield.getText();
	    					
	    					String query = "SELECT * FROM Reviews WHERE Product ='"+product+"'";
							ResultSet q = stmt.executeQuery(query);
							while(q.next())
								resultArea.append(q.getString("ReviewContent")+"\n");
							
							SQLOut.append(query);
							
							ConnectDB.closeConnection(conn);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}           	
						setSQLOutput(SQLOut);
	                }
	            });


	            

	        }
			});
		//Like Reviews
		buttonPanel.buttons[8].addActionListener(new ActionListener() {
	           
	        public void actionPerformed(ActionEvent e) {
	        	final Frame4 frame=new Frame4("Review ID : ","Like it");
	            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	            frame.setVisible(true);

	            frame.btn1.addActionListener(new ActionListener() {
	               
	                public void actionPerformed(ActionEvent e) {
	                	/*Fill this function*/
	                	/*Press "Like Reviews" button, a new window will pop out. */
	                	/*Input "Review ID" and press "Like it", this information should be inserted into database.*/
	                	String likeID = null;
	                	Integer like = 0;
	                	
	                	String querylID = "SELECT * FROM ReviewLike";
	                	
	                						
						conn=ConnectDB.openConnection();


		              	SQLOut.append(querylID+"\n\n");
		              	resultArea.setText("");
		              	Statement stmt;
						try {
							stmt = conn.createStatement();
							ResultSet l = stmt.executeQuery(querylID);
	    					while(l.next())
	    						likeID = l.getString("LikeID");
	    					
	    					System.out.println(likeID);
	    					likeID = likeID.replace("L", "");
	    					like = Integer.parseInt(likeID.trim());
	    					like++;
	    					likeID = "L" + like;	
	    					
	    					String customer = loginPanel.username.getText();
	    					String review = frame.txtfield.getText();	    						    					    						    				
	    					
	    					String insert = "INSERT INTO ReviewLike VALUES('"+likeID+"','"+customer+"','"+review+"')";
							stmt.executeQuery(insert);
							SQLOut.append(insert);
							
							ConnectDB.closeConnection(conn);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}           	
						setSQLOutput(SQLOut); 
	                }
	            });	         
	        }
			});	
		//List All Likes
		buttonPanel.buttons[9].addActionListener(new ActionListener() {
	          
            public void actionPerformed(ActionEvent e) {
            	StringBuffer result= new StringBuffer();
            	/*Fill this function*/
            	/*Press "List All Likes" button, all reviews that liked by this customer should be shown in the result panel.*/
            	conn=ConnectDB.openConnection();

              	resultArea.setText("");
              	Statement stmt;
              	Statement stmt1;
				try {
					stmt = conn.createStatement();
					
					String customer = loginPanel.username.getText();
					String rid = null;
					String query = "SELECT * FROM ReviewLike WHERE Customer ='"+customer+"'";
					SQLOut.append(query+"\n");
					ResultSet q = stmt.executeQuery(query);
					while(q.next())
					{
						rid = q.getString("Review");
						System.out.println(rid);
						String queryR = "SELECT * FROM Reviews WHERE ReviewID ='"+rid+"'";
						
						stmt1 = conn.createStatement();
						ResultSet r = stmt1.executeQuery(queryR);
						while(r.next())
						{
							for(int i =1;i<7;i++)
								resultArea.append(r.getString(i));
						}
						resultArea.append("\n");
					}

					
					
					ConnectDB.closeConnection(conn);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}           	
				setSQLOutput(SQLOut);

            }
        });
		//Nearest Sellers
		buttonPanel.buttons[10].addActionListener(new ActionListener() {
	          
            public void actionPerformed(ActionEvent e) {
            	StringBuffer result= new StringBuffer();
            	/*Fill this function*/
            	/*Press "Nearest Seller" button, the nearest seller info for this customer should be shown in the result panel.*/
            	/*This is a spatial query*/
            	 conn=ConnectDB.openConnection();
            	//find info
            	String customer = loginPanel.username.getText();
              	String QueryStrS = "SELECT AddressID FROM Sellers";
              	String cl = "SELECT * FROM Address WHERE addrid = (SELECT AddressID FROM Customers WHERE Email ='"+customer+"')";
             	String cLa = null;
             	String cLo = null;
              	String sLa = null;
              	String sLo = null;
              	SQLOut.append(QueryStrS+"\n"+cl+"\n\n");
              	resultArea.setText("");
              	
              	Statement stmt;
              	Statement stmt1;
              	Statement stmt2;
				try {
					stmt = conn.createStatement();

					ResultSet cLoca = stmt.executeQuery(cl);
					if(cLoca.next())
					{
						cLa = cLoca.getString("Latitude");
						cLo = cLoca.getString("Longitude");
					}
					//SQLOut.append(cLa+"\n"+cLo+"\n\n");
					
					Integer min = 99999999;
					Integer minus = 0;
					String minLa = null;
					String minLo = null;
					
					stmt1 = conn.createStatement();
					
					ResultSet sA = stmt.executeQuery(QueryStrS);
					while(sA.next())
					{
						String se = sA.getString("AddressID");
						String seller = "SELECT * FROM Address WHERE AddrID ='"+se+"'";
						
						stmt2 = conn.createStatement();
						ResultSet sLoca = stmt2.executeQuery(seller);
						while(sLoca.next())
						{
							sLa = sLoca.getString("Latitude");
							sLo = sLoca.getString("Longitude");
							//System.out.println(sLa+" "+sLo+"\n");
							minus = Math.abs(Integer.parseInt(cLa.trim())-Integer.parseInt(sLa.trim()))+Math.abs(Integer.parseInt(cLo.trim())-Integer.parseInt(sLo.trim()));
							//System.out.println(minus+"\n");
							if(minus < min)
							{
								min = minus;
								minLa = sLa;
								minLo = sLo;
							}							
						}
						
					}
					//System.out.println(minLa+" "+minLo+"\n");
					String seller = "SELECT * FROM Sellers WHERE AddressID = (SELECT AddrID FROM Address WHERE Latitude='"+minLa+"' AND Longitude ='"+minLo+"')";
					SQLOut.append(seller);
					ResultSet s = stmt.executeQuery(seller);
					if(s.next())
					{
						for(int i = 1;i<14;i++)
							resultArea.append(s.getString(i));
					}
					s.close();
					
					ConnectDB.closeConnection(conn);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}           	
				setSQLOutput(SQLOut); 
            	
            	
            }
        });
		//Range Seller Search
		buttonPanel.buttons[11].addActionListener(new ActionListener() {
           
            public void actionPerformed(ActionEvent e) {
            	final Frame3 frame=new Frame3("Please input coordinate: ");
                frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                frame.setVisible(true);

                frame.btn1.addActionListener(new ActionListener() {
                  
                    public void actionPerformed(ActionEvent e) {
                    	/*Fill this function*/
                    	/*Press this Button, input left top corner coordinate and right down corner coordinate*/
                    	/*press ok, you should be able list the information(including address information) about seller who lives in this area. Close query window*/
                    	/*This is a spatial query*/
                    	conn=ConnectDB.openConnection();
                    	
                    	resultArea.setText("");
                    	String tX = frame.txtfield[0].getText();
                    	String tY = frame.txtfield[1].getText();
                    	String bX = frame.txtfield[2].getText();
                    	String bY = frame.txtfield[3].getText();
                    	
                    	//resultArea.append(tX+" "+tY+" "+bX+" "+bY);
                    	String address = "SELECT AddrID FROM Address WHERE Latitude > "+ tX +" AND Latitude < "+bX+" AND Longitude > "+tY+" AND Longitude < "+bY;
                    	//resultArea.append(address);
                    	SQLOut.append(address+"\n");
                    	
                    	Statement stmt;
                    	Statement stmt1;
                    	try {
							stmt = conn.createStatement();
							
							ResultSet ad = stmt.executeQuery(address);
							while(ad.next())
							{
								String seller = "SELECT * FROM Sellers WHERE AddressID = '"+ad.getString("AddrID")+"'";
								SQLOut.append(seller+"\n");
								
								stmt1 = conn.createStatement();
								ResultSet se = stmt1.executeQuery(seller);
								while(se.next())
								{
									for(int i = 1; i < 14;i++)
										resultArea.append(se.getString(i));
								}
								resultArea.append("\n");
							}
							
							ConnectDB.closeConnection(conn);
							
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                    	setSQLOutput(SQLOut); 
    					
                    }
                });
            }
        });
		
	}

	
	public void setSQLOutput(StringBuffer sb)
	{
		sqlPanel.SQLArea.setText(sb.toString());
		sqlPanel.SQLArea.setEnabled(true);
	}
	public void setSqlPanel(){
		sqlPanel = new SqlPanel();
		showLabel = new JLabel("The corresponding SQL sentence:");
		showLabel.setBounds(30, 490, 400, 20);
		sqlPanel.setBounds(5, 515,790, 150);
		this.add(sqlPanel);
		this.add(showLabel);
	}
	public void setResultPanel(){
		resultPanel = new ResultPanel();
		resultArea = new JTextArea(10,30);
		resultArea.setLineWrap(true);
		scrollPane = new JScrollPane(resultArea);
		headLabel = new JLabel("Shopping System");
		add(scrollPane);
		add(headLabel);
		
		headLabel.setFont(new Font("Serif", Font.BOLD, 30));
		headLabel.setBounds(240,20 , 360, 60);
		scrollPane.setBounds(20, 100,740, 250);
	}

	public void setLoginPanel(){
		loginPanel = new LoginPanel();
		this.add(loginPanel);

		loginPanel.signup.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
		           signUpPanel.enablePanel();
			}
        });
        loginPanel.login.addActionListener(new ActionListener() {
           
            public void actionPerformed(ActionEvent e) {  
            //	buttonPanel.enablePanel();
            	/*Fill this function*/
            	/*Press this Button, you should be able match the user information. If valid, keep the user email information(but can't modified) and clear the password*/
            	/*If invalid, you should pop up a dialog box to notify user, then enable signup panel for user to register*/
            	/*After logged in, you should change this button's function as logout which means disable all the panel, return to the original state*/
            	if(trigger==0){
               	 //match account
               	 conn=ConnectDB.openConnection();
               	 //String QueryStr = "SELECT Email FROM Customers WHERE Email='bill@csci585.edu' AND Passwd = '123'";
               	 
               	 String QueryStrC = "SELECT Email FROM Customers WHERE Email='"+loginPanel.username.getText()+"' AND Passwd = '"+loginPanel.password.getText()+"'";
               	 String QueryStrS = "SELECT Email FROM Sellers WHERE Email='"+loginPanel.username.getText()+"' AND Passwd = '"+loginPanel.password.getText()+"'";
               	 SQLOut.append(QueryStrC+"\n"+QueryStrS+"\n\n");            
               	 try {
   					Statement stmt = conn.createStatement();
   					Statement stmt1 = conn.createStatement();
   					//ResultSet re = stmt.executeQuery(QueryStr);
   					
   					ResultSet reC = stmt.executeQuery(QueryStrC);
   					ResultSet reS = stmt1.executeQuery(QueryStrS);
   					
   					/*System.out.println(re.next());
   					while(re.next())
   						System.out.println(re.getString("Email"));*/
   					
   					if(reC.next()||reS.next())
   	            	 {
   	            		 loginPanel.setUserName(loginPanel.username.getText().toString());
   	            		 loginPanel.disablePanel();
   	            		 loginPanel.password.setText("");
   	            		 trigger = 1;
   	            		 loginPanel.login.setText("logout");                                               
   	            		 signUpPanel.disablePanel();
   	            		 buttonPanel.enablePanel();
  	            		 loginPanel.signup.setEnabled(false);
   	            	 }
   	            	 else
   	            	 {
  	            		 JOptionPane.showMessageDialog(null, "No user existed. Please signup!");
   	            		 signUpPanel.enablePanel();
   	            		// loginPanel.disablePanel();  
   	            		buttonPanel.enablePanel();
   	            	}
   					 ConnectDB.closeConnection(conn);

   				} catch (SQLException e1) {
   					// TODO Auto-generated catch block
   					e1.printStackTrace();
   					ConnectDB.closeConnection(conn);
   					return;
   				}

               	//getnotification

               	 //trigger = 1
               }else{
               	loginPanel.login.setText("login");
               	loginPanel.enablePanel();
               	loginPanel.signup.setEnabled(true);
               	loginPanel.password.setText("");
               	loginPanel.username.setText("");
               	disableResult();
               	trigger = 0;
               	buttonPanel.disablePanel();
           	}
               	setSQLOutput(SQLOut);
              	}

           });
   

	}

	public void setSignupPanel(){

		signUpPanel = new SignupPanel();
		this.add(signUpPanel);
		signUpPanel.signup.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {  
            	/*Fill this function*/
            	/*Press this signup button, you should be able check whether current account is existed. If existed, pop up an error, if not check input validation(You can design this part according to your database table's restriction) create the new account information*/
            	/*pop up a standard dialog box to show <succeed or failed>*/
            	conn=ConnectDB.openConnection();
            	//check email
            	String CheckEmail = "SELECT Email FROM Customers WHERE Email='"+signUpPanel.email.getText()+"'";
            	String CheckEmail1 = "SELECT Email FROM Sellers WHERE Email='"+signUpPanel.email.getText()+"'";
              	SQLOut.append(CheckEmail+"\n\n");
              	Statement stmt;
              	Statement stmt3;
				try {
					stmt = conn.createStatement();
					stmt3 = conn.createStatement();
					ResultSet re = stmt.executeQuery(CheckEmail);
					ResultSet re1 = stmt3.executeQuery(CheckEmail1);
					if(re.next()||re1.next())
						JOptionPane.showMessageDialog(null, "This user name is already existed, please log in!");
					
					String email = signUpPanel.email.getText();
					if(email.matches("[\\s\\S]*@[\\s\\S]*.[\\s\\S]*"))
							;
					else
						JOptionPane.showMessageDialog(null, "Wrong format of email!");
					
					ConnectDB.closeConnection(conn);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				//check pw
				String pw = signUpPanel.password.getText();
				String pw2 = signUpPanel.password2.getText();
				if(pw.equals(pw2))
					;
				else
					JOptionPane.showMessageDialog(null, "ReEnter Password doesn't match the first one!");
				//check birthday format
				if(signUpPanel.birthday.getText() == null || signUpPanel.birthday.getText().equals(""))
					;
				else
				{
					if(signUpPanel.birthday.getText().matches("\\d{4}-\\d{1,2}-\\d{1,2}"))
						;
					else
						JOptionPane.showMessageDialog(null, "The format of birthday must be 'YYYY-MM-DD'!");
				}
				//check strno&zip
				String str_no = signUpPanel.str_no.getText();
				String zip = signUpPanel.zip.getText();
				if(str_no.matches("[0-9]*"))
					;
				else
					JOptionPane.showMessageDialog(null, "Street number must be numbers!");
				if(zip.matches("[0-9]*"))
						;
					else
						JOptionPane.showMessageDialog(null, "Zip code must be numbers!");
				//check all input filled
				if((signUpPanel.email.getText() == null || signUpPanel.email.getText().equals(""))||
					(signUpPanel.password.getText() == null || signUpPanel.password.getText().equals(""))||
					(signUpPanel.password2.getText() == null || signUpPanel.password2.getText().equals(""))||
					(signUpPanel.fname.getText() == null || signUpPanel.fname.getText().equals(""))||
					(signUpPanel.lname.getText() == null || signUpPanel.lname.getText().equals(""))||
					(signUpPanel.birthday.getText() == null || signUpPanel.birthday.getText().equals(""))||
					(signUpPanel.str_no.getText() == null || signUpPanel.str_no.getText().equals(""))||
					(signUpPanel.str_address.getText() == null || signUpPanel.str_address.getText().equals(""))||
					(signUpPanel.city.getText() == null || signUpPanel.city.getText().equals(""))||
					(signUpPanel.state.getText() == null || signUpPanel.state.getText().equals(""))||
					(signUpPanel.zip.getText() == null || signUpPanel.zip.getText().equals("")))
					JOptionPane.showMessageDialog(null, "All fields should be filled!");
				else
				{
					conn=ConnectDB.openConnection();
					
					//SQLOut.append(insert);
					String queryAddrID = "SELECT * FROM Address";
					
					String addrID = null;
					Integer addr = 0;
					
					
					Statement stmt1,stmt2;
					try {
						stmt1 = conn.createStatement();
						stmt2 = conn.createStatement();
						
						ResultSet id = stmt2.executeQuery(queryAddrID);
						while(id.next())
							addrID = id.getString("AddrID");
						addrID = addrID.replace("A", "");
						addr = Integer.parseInt(addrID.trim());
						addr++;
						addrID = "A" + addr.toString();
						System.out.println(addrID);
						String insertAddr = "INSERT INTO Address VALUES('"+addrID+"','"+signUpPanel.str_address.getText()+"','"+signUpPanel.str_address.getText()+"','"+signUpPanel.city.getText()+"','"+signUpPanel.state.getText()+"','"+signUpPanel.zip.getText()+"',"+null+","+null+","+null+")";
						stmt1.executeQuery(insertAddr);
						String insert = "INSERT INTO Customers VALUES('"+signUpPanel.email.getText()+"','"+signUpPanel.password.getText()+"','"+signUpPanel.fname.getText()+"','"+signUpPanel.lname.getText()+"',to_date('"+signUpPanel.birthday.getText()+"','YYYY-MM-DD'),"+null+","+null+","+null+","+null+","+null+","+null+",'"+addrID+"',"+null+")";
						stmt1.executeQuery(insert);
						
						SQLOut.append(insert+"\n"+queryAddrID+"\n");
						System.out.println(insertAddr);
						//JOptionPane.showMessageDialog(null, "Sign up succeed!");
						id.close();
						stmt1.close();
						stmt2.close();
						ConnectDB.closeConnection(conn);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				setSQLOutput(SQLOut);
            }
        });

		signUpPanel.disablePanel();

	}


}


class ConnectDB{

	public static Connection openConnection(){
        try{
	        String driverName = "oracle.jdbc.driver.OracleDriver";
	        Class.forName(driverName);

	        //set the username and password for your connection.
	        String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	        String uname = "SYSTEM";
	        String pwd = "12345678";

	        return DriverManager.getConnection(url, uname, pwd);
        }
        catch(ClassNotFoundException e){
        	System.out.println("Class Not Found");
        	e.printStackTrace();
        	return null;
        }
        catch(SQLException sqle){
        	System.out.println("Connection Failed");
        	sqle.printStackTrace();
        	return null;
        }

	}
	public static void closeConnection(Connection conn)
	{
		try{
		 conn.close();
	 }
	    catch (Exception e){
	    	e.printStackTrace();
	    	System.out.println("connection closing error ");
	    }
	}
}
public class Assignment2 {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
    	MainFrame frame = new MainFrame();
    	frame.setVisible(true);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
