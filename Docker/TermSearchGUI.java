import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class TermSearchGUI implements ActionListener
{
	JFrame window;
    Container content;
    JPanel cards, panelSelectFiles, panelSelectAction, panelEnterSearchTerm, panelSearchResults, panelEnterNValue, panelTopNResults;
    JCheckBox checkboxHugo, checkboxShakespeare, checkboxTolstoy;
    JButton buttonLoadEngine, buttonSearchForTerm, buttonTopN, buttonSearch, buttonSearchN, buttonExit;
    JTextField textFieldSearchTerm, textFieldNValue;
    int nVal = -1;

    public TermSearchGUI()
    {
    	window = new JFrame("Term Search");
        content = window.getContentPane();
        content.setLayout(new GridLayout(2,1));
        
        ////////// MENU FOR SELECTING FILES //////////
        
        panelSelectFiles = new JPanel();
        panelSelectFiles.setLayout(new GridLayout(5,1));
        
        JLabel labelSelectFiles = new JLabel("Select Files to Search:");
        labelSelectFiles.setFont(new Font("TimesNew", Font.BOLD, 32));
        
        checkboxHugo = new JCheckBox("Hugo.tar.gz");
        checkboxHugo.setSelected(true);
        
        checkboxShakespeare = new JCheckBox("shakespeare.tar.gz");
        checkboxShakespeare.setSelected(true);
        
        checkboxTolstoy = new JCheckBox("Tolstoy.tar.gz");
        checkboxTolstoy.setSelected(true);
        
        buttonLoadEngine = new JButton("Load Engine");
        buttonLoadEngine.setFont(new Font("TimesNew", Font.ITALIC + Font.BOLD, 24));
        buttonLoadEngine.addActionListener(this);
        
        panelSelectFiles.add(labelSelectFiles);
        panelSelectFiles.add(checkboxHugo);
        panelSelectFiles.add(checkboxShakespeare);
        panelSelectFiles.add(checkboxTolstoy);
        panelSelectFiles.add(buttonLoadEngine);
        
        //////////////////////////////////////////////
        
        ///////// MENU FOR SELECTING ACTION //////////
        
        panelSelectAction = new JPanel();
        panelSelectAction.setLayout(new GridLayout(3,1));
	     
	    JLabel labelSelectAction = new JLabel("Please make a selection:");
	    labelSelectAction.setFont(new Font("TimesNew", Font.BOLD, 32));
	     
	    buttonSearchForTerm = new JButton("Search for Term");
	    buttonSearchForTerm.setFont(new Font("TimesNew", Font.ITALIC + Font.BOLD, 24));
	    buttonSearchForTerm.addActionListener(this);
	     
	    buttonTopN = new JButton("Top-N");
	    buttonTopN.setFont(new Font("TimesNew", Font.ITALIC + Font.BOLD, 24));
	    buttonTopN.addActionListener(this);
	    
	    panelSelectAction.add(labelSelectAction);
	    panelSelectAction.add(buttonSearchForTerm);
	    panelSelectAction.add(buttonTopN);
	    
	    //////////////////////////////////////////////
	    
	    //////// MENU FOR SEARCHING FOR TERM /////////
        
        panelEnterSearchTerm = new JPanel();
        panelEnterSearchTerm.setLayout(new GridLayout(10,1));
	     
	    JLabel labelEnterSearchTerm = new JLabel("Enter Search Term");
	    labelEnterSearchTerm.setFont(new Font("TimesNew", Font.BOLD, 32));
	    
	    textFieldSearchTerm = new JTextField(25);
	    textFieldSearchTerm.addActionListener(this);
	     
	    buttonSearch = new JButton("Search");
	    buttonSearch.setFont(new Font("TimesNew", Font.ITALIC + Font.BOLD, 24));
	    buttonSearch.addActionListener(this);
	    
	    panelEnterSearchTerm.add(labelEnterSearchTerm);
	    panelEnterSearchTerm.add(textFieldSearchTerm);
	    panelEnterSearchTerm.add(buttonSearch);
	    
	    //////////////////////////////////////////////
	    
	    /////////////// SEARCH RESULTS ///////////////
        
	    panelSearchResults = new JPanel();
	    panelSearchResults.setLayout(new GridLayout(5,1));
	     
	    JLabel labelSearchResults = new JLabel("Search Results");
	    labelSearchResults.setFont(new Font("TimesNew", Font.BOLD, 32));
	    
	    panelSearchResults.add(labelSearchResults);
	    
	    //////////////////////////////////////////////
	    
	    ///////// MENU FOR ENTERING N VALUE //////////
        
        panelEnterNValue = new JPanel();
        panelEnterNValue.setLayout(new GridLayout(5,1));
	     
	    JLabel labelEnterNValue = new JLabel("Enter N Value");
	    labelEnterNValue.setFont(new Font("TimesNew", Font.BOLD, 32));
	    
	    textFieldNValue = new JTextField(10);
	    textFieldNValue.addActionListener(this);
	     
	    buttonSearchN = new JButton("Search");
	    buttonSearchN.setFont(new Font("TimesNew", Font.ITALIC + Font.BOLD, 24));
	    buttonSearchN.addActionListener(this);
	    
	    panelEnterNValue.add(labelEnterNValue);
	    panelEnterNValue.add(textFieldNValue);
	    panelEnterNValue.add(buttonSearchN);
	    
	    //////////////////////////////////////////////
	    
	    //////////////// TOP N RESULTS ///////////////
        
	    panelTopNResults = new JPanel();
	    panelTopNResults.setLayout(new GridLayout(5,1));
   
	    JLabel labelTopNResults = new JLabel("Top-N Results");
	    labelTopNResults.setFont(new Font("TimesNew", Font.BOLD, 32));
  
	    panelTopNResults.add(labelTopNResults);
  
	    //////////////////////////////////////////////
	     
	    cards = new JPanel(new CardLayout());
	    cards.add(panelSelectFiles, "panelSelectFiles");
	    cards.add(panelSelectAction, "panelSelectAction");
	    cards.add(panelEnterSearchTerm, "panelEnterSearchTerm");
	    cards.add(panelSearchResults, "panelSearchResults");
	    cards.add(panelEnterNValue, "panelEnterNValue");
	    cards.add(panelTopNResults, "panelTopNResults");
	    
	    JPanel exitPanel = new JPanel();
	    exitPanel.setLayout(new FlowLayout());
	    buttonExit = new JButton("Exit");
	    buttonExit.setFont(new Font("TImesNew", Font.BOLD, 24));
	    buttonExit.addActionListener(this);
	    exitPanel.add(buttonExit);
	     
	    window.add(cards);
	    window.add(exitPanel);
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    window.setSize(900, 600);
	    //window.pack();
	    window.setVisible(true);
    }

    public void actionPerformed(ActionEvent e)
    {
        Component buttonClicked = (Component) e.getSource();
        CardLayout cl = (CardLayout)(cards.getLayout());
        
        if(buttonClicked == buttonLoadEngine)
        {
            if(!checkboxHugo.isSelected() && !checkboxShakespeare.isSelected() && !checkboxTolstoy.isSelected())
            {
            	JOptionPane.showMessageDialog(null, "You must select at least one file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
	            System.out.println("Engine should be loaded here. Files to search:");
	            if(checkboxHugo.isSelected())
	            	System.out.println("- Hugo.tar.gz");
	            if(checkboxShakespeare.isSelected())
	            	System.out.println("- shakespeare.tar.gz");
	            if(checkboxTolstoy.isSelected())
	            	System.out.println("- Tolstoy.tar.gz");
	            
	        	cl.show(cards, "panelSelectAction");
            }
        }
        else if(buttonClicked == buttonSearchForTerm)
        {
        	cl.show(cards, "panelEnterSearchTerm");
        }
        else if(buttonClicked == buttonTopN)
        {
        	cl.show(cards,  "panelEnterNValue");
        }
        else if(buttonClicked == buttonSearch)
        {
        	System.out.println("Searched for \'" + textFieldSearchTerm.getText() + "\'");
        	
        	cl.show(cards,  "panelSearchResults");
        }
        else if(buttonClicked == buttonSearchN)
        {
        	try
        	{
        		int tempN = Integer.parseInt(textFieldNValue.getText());
        		
        		if(tempN > 0)
        		{
        			nVal = tempN;
        			
        			System.out.println("Request for Top-N, N=" + nVal);
        			cl.show(cards, "panelTopNResults");
        		}
        		else
        			throw new IllegalArgumentException("Invalid N");
        	}
        	catch (Exception ex)
        	{
        		JOptionPane.showMessageDialog(null, "Please enter a valid value for N (positive integer).", "Error", JOptionPane.ERROR_MESSAGE);
        	}
        }
        else if(buttonClicked == buttonExit)
        {
        	System.exit(0);
        }
        
    }

    public static void main(String[] args)
    {
        new TermSearchGUI();
    }
}
