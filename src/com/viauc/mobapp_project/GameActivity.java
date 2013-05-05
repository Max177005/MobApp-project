package com.viauc.mobapp_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class GameActivity extends Activity {
	
	
	    public static final int EMPTY = 0;			
		//MODE_1P => AI vs Player , MODE_2P => PvP
		public static int game_mode = Constants.MODE_1P;				
		public static int score_player_1 = 0;
		public static int score_player_2 = 0;		
	
	
		// number of turns
		private int count = 0;
		// game board 3x3
		private int board[][] =  {
								{0,0,0},
								{0,0,0},
								{0,0,0}
							   };	
		
		private CharSequence player_name_1 = "Player 1";
		private CharSequence player_name_2 = "Player 2";		
		private int player = 1;
		private int player_symbol = 0;	
	
	
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.game);

	    	final ImageButton b3 = (ImageButton) findViewById(R.id.b3);
	        final ImageButton b2 = (ImageButton) findViewById(R.id.b2);
	        final ImageButton b1 = (ImageButton) findViewById(R.id.b1);

	        final ImageButton b6 = (ImageButton) findViewById(R.id.b6);
	        final ImageButton b5 = (ImageButton) findViewById(R.id.b5);
	        final ImageButton b4 = (ImageButton) findViewById(R.id.b4);
	        
	        final ImageButton b9 = (ImageButton) findViewById(R.id.b9);
	        final ImageButton b8 = (ImageButton) findViewById(R.id.b8);
	        final ImageButton b7 = (ImageButton) findViewById(R.id.b7);        

	        b1.setOnClickListener(button_listener);
	        b2.setOnClickListener(button_listener);
	        b3.setOnClickListener(button_listener);
	        
	        b4.setOnClickListener(button_listener);
	        b5.setOnClickListener(button_listener);
	        b6.setOnClickListener(button_listener);
	        
	        b7.setOnClickListener(button_listener);
	        b8.setOnClickListener(button_listener);
	        b9.setOnClickListener(button_listener);        

	        b1.setClickable(true);
	        b2.setClickable(true);
	        b3.setClickable(true);
	        
	        b4.setClickable(true);
	        b5.setClickable(true);
	        b6.setClickable(true);
	        
	        b7.setClickable(true);
	        b8.setClickable(true);
	        b9.setClickable(true);	        

			updateScore(0);	

			for (int i = 0; i < 3; i++)
				for (int j = 0; j < 3; j++)
					board[i][j] = 0; 

			if ((game_mode == Constants.MODE_1P) && (count % 2 != 0))
				CompGame();
	    }
	  
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.activity_main, menu);
	        return true;
	    }
	   
	    OnClickListener button_listener = new View.OnClickListener() {
	        public void onClick(View v) {
	            ImageButton ibutton = (ImageButton) v;
	        	ibutton.setClickable(false);
	        	count++;        	
	            if ((count % 2 != 0) && (game_mode == Constants.MODE_2P)) {
	            	player = 1;
	                ibutton.setImageResource(R.drawable.cross);
	            }
	            else if ((count % 2 == 0) || (game_mode == Constants.MODE_1P)) {
	            	player = 2;
	            	if ((player_symbol == 0) && (game_mode == Constants.MODE_1P))
	            		ibutton.setImageResource(R.drawable.dot);
	            	else if ((player_symbol == 1) && (game_mode == Constants.MODE_1P))
	            		ibutton.setImageResource(R.drawable.cross);
	            	else ibutton.setImageResource(R.drawable.dot);
	            }
	        	afterMove(ibutton);
	        }
	    };
	    
	    
	    public boolean isFull () {
	    	for (int i = 0; i < 3; i++)
	    		for (int j = 0; j < 3; j++)
	    			if (board[i][j] == 0)
	    				return false;				
	    	return true;
	    }
	    
	    public int getBoardValue(int i,int j) {
	        if(i < 0 || i >= 3 || j < 0 || j >= 3) 
	        	return EMPTY;
	        return board[i][j];
	    }	    

	    public int [] nextWinningMove(int current_player) {
	        for(int i=0;i<3;i++)
	            for(int j=0;j<3;j++)            
	                if(getBoardValue(i, j)==EMPTY) {
	                    board[i][j] = current_player;
	                    boolean win = isWin(current_player);
	                    board[i][j] = EMPTY;
	                    if(win) 
	                    	return new int[]{i,j};
	                }
	        return null;
	    }
	    	    
	    /*
	     * AI 
	     *  1 - Tries to win in 1 move
	     *  2 - Prevents the enemy from winning
	     *  3 - Center position on the board ("lucky position")
	     *  4 - First available position on the board
	     *  5 - No move available
	     * */
	    
	    public int [] nextMove(int current_player) {
            
	    	//1
	        int winMove[] = nextWinningMove(current_player);
	        if(winMove != null) 
	        	return winMove;

	        //2
	        for(int i=0;i<3;i++) 
	            for(int j=0;j<3;j++)
	                if(getBoardValue(i, j)==EMPTY){
	                    board[i][j] = current_player;
	                    boolean ok = nextWinningMove(current_player == 1 ? 2 : 1 ) == null;                    
	                    board[i][j] = EMPTY;
	                    if(ok) 
	                    	return new int[]{i,j};
	                }
	        
	        //3
	        if(getBoardValue(1, 1)==EMPTY) 
	        	return new int[]{1,1};
	        
	        //4
	        for(int i=0;i<3;i++) 
	            for(int j=0;j<3;j++)
	                if(getBoardValue(i, j)==EMPTY)
	                    return new int[]{i,j};
	        
	        //5
	        return null;
	    }    
	    

	    public boolean isWin(int current_player) {
	        
	    	int DI[] = {-1,0,1,1};
	        int DJ[] = { 1,1,1,0};
	        
	        for(int i=0; i < 3; i++)
	            for(int j=0; j < 3; j++){    
	                if(getBoardValue(i, j) != current_player) 
	                	continue;
	                
	                for(int k = 0; k < 4; k++) {
	                    int c = 0;
		                while(getBoardValue(i + DI[k] * c, j + DJ[k] * c) == current_player){
		                	c++;
		                	if(c == 3) 
		                    	return true;
		                }		                   
	                }
	            }
	        return false;
	    }
		    
	    
	    public void afterMove(ImageButton ib) {
	    	
	    	CharSequence pos_str = (CharSequence) ib.getTag();	
	    	int pos = (int) pos_str.charAt(0) - 48;

	    	if (player == 1) {
	    		if (pos < 4)				
	    			board[0][pos - 1] = 1;
	    		else if (pos < 7) 
	    			board[1][(pos - 1) % 3] = 1;
	    		else if (pos < 10)
	    			board[2][(pos - 1) % 3] = 1;
	    	}
	    	else {
	    		if (pos < 4)				
	    			board[0][pos - 1] = 2;
	    		else if (pos < 7) 
	    			board[1][(pos - 1) % 3] = 2;
	    		else if (pos < 10)
	    			board[2][(pos - 1) % 3] = 2;
	    	}
	    	
	    	// Winner ?
	    	boolean result = isWin(player);	 
	    	if (result) {  
	    		if (player == 1) {
	    			updateScore(1);
	    			if (game_mode == Constants.MODE_2P)
	    				gameOver("Congrats. " + player_name_1 + " wins !!");	    			
	    			else gameOver("Computer Wins !!");	    			
	    		}
	    		else {
	    			updateScore(2);
	    			if (game_mode == Constants.MODE_2P)
	    				gameOver("Congrats. " + player_name_2 + " wins !!");	    			
	    			else gameOver("Congrats. You have won !!");	    			
	    		}
	    		return;	    	
	    	}
	    	// Board is full ?
	    	if(isFull()){
	    		gameOver("Nobody wins!");
	    		return;
	    	}	    	
	    	// AI enabled mode ? Computer's turn
	    	if(game_mode == Constants.MODE_1P && player == 2){
			   	CompGame();
	    	}

	    }
	    public void updateScore(int player_number) {	    	
	    	TextView tv = (TextView) findViewById(R.id.scoreboard);	    	
	    	if(player_number == 1)
	    		score_player_1++;
	    	else if(player_number == 2)
	    		score_player_2++;

	    	if(game_mode == Constants.MODE_1P) {
	    		player_name_1 = "Computer";
	    		player_name_2 = "You";
	    	}	    		
	    	CharSequence score_txt = player_name_1 + " : " + score_player_1 + " - " + score_player_2 + " : " + player_name_2;	    	
	    	tv.setText(score_txt);
	    }	    

	    
	    public boolean gameOver(CharSequence message)	
	    {   
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage(message)
	        			.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
	        				public void onClick(DialogInterface dialog, int id) {
	        					Intent intent = new Intent(Intent.ACTION_VIEW);
	        		            intent.setClass(GameActivity.this, GameActivity.class);
	        		            finish();
	        					startActivity(intent);
	        				}
	        			});
	        AlertDialog alert = builder.create();
	        alert.show();
	        return true;
	    }
	    
   
	    public void CompGame() {
	    	player = 1;
	    	count++;	    	
	    	int [] next_pos = new int[2];
	    	next_pos = nextMove(player);	    	
	    	compPlay(next_pos[0], next_pos[1]);
	    }
	
	    public void compPlay (int x, int y) {	    	
	       	ImageButton ib_tmp = (ImageButton) findViewById(R.id.b1);
	       	int ib_id = ib_tmp.getId();	
	       	
	       	if ((x == 0) && (y == 0)) { /*initial value*/ }
	       	else {
	       		if (x == 0)
	       			ib_id -= y;	
	       		else if (x == 1)
	       			ib_id += (3 - y);
	       		else if (x == 2)
	       			ib_id += (6 - y);	
	       	}
	       	ImageButton ib = (ImageButton) findViewById (ib_id);
	       	if (player_symbol == 0)
	       		ib.setImageResource(R.drawable.cross);
	       	else ib.setImageResource(R.drawable.dot);	       	
	       	ib.setClickable(false);
	        afterMove(ib);
	    }
	    
	   

}
