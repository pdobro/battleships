package model;

public class BoardPlayer {

	private Ship[][] board = new Ship[10][10];

	public BoardPlayer() {
		for (int row = 0; row < board[0].length; row++) {
			for (int column = 0; column < board.length; column++) {
				board[row][column] = null;
			}
		}
	} 
	
	
	public boolean shoot(int x, int y) {

		if(board[x][y] != null) {
			board[x][y].hit();
			return true;
		}
		return false;
	}
	

    public boolean placeShip(Ship ship, int x, int y) {
        if (canPlaceShip(ship, x, y)) {
            int length = ship.size;

            if (ship.vertical) {
                for (int i = y; i < y + length; i++) {
                		board[x][i] = ship;
                    }
            }
            else {
                for (int i = x; i < x + length; i++) {
                		board[i][y] = ship;
                    }
                }
            return true;
            }
        return false;
    }
    
    
    public boolean canPlaceShip (Ship ship, int x, int y) {
    	int length = ship.size;
    	if(ship.vertical) {
    		for (int i = y; i < y + length; i++) {
    			if(x >= 10)
    				return false;
    			if(i >= 10)
    				return false;
    			if (board[x][i] != null)
    				return false;
				if(x > 0) {
					if(board[x-1][i] != null)
						return false;
				}
				if(x < 9) {
					if(board[x+1][i] != null)
						return false;
				}
    			if(i == y) {
    				if(i > 0) {
    					if(board[x][i-1] != null)
    						return false;
						if(x > 0) {
							if(board[x-1][i-1] != null)
								return false;
						}
						if(x < 9) {
							if(board[x+1][i-1] != null)
								return false;
						}
					}
				}
    			if(i == y + length - 1) {
					if(i < 9) {
						if(board[x][i+1] != null)
							return false;
						if(x > 0) {
							if(board[x-1][i+1] != null)
								return false;
						}
						if(x < 9) {
							if(board[x+1][i+1] != null)
								return false;
						}
					}
				}
            }
    	} else {
            for (int i = x; i < x + length; i++) {
				if (i >= 10)
					return false;
				if(y >= 10)
					return false;
				if (board[i][y] != null)
					return false;
				if(y > 0) {
					if(board[i][y-1] != null)
						return false;
				}
				if(y < 9) {
					if(board[i][y+1] != null)
						return false;
				}
				if(i == x) {
					if(i > 0) {
						if(board[i-1][y] != null)
							return false;
						if(y > 0) {
							if(board[i-1][y-1] != null)
								return false;
						}
						if(y < 9) {
							if(board[i-1][y+1] != null)
								return false;
						}
					}
				}
				if(i == x + length - 1) {
					if(i < 9) {
						if(board[i+1][y] != null)
							return false;
						if(y > 0) {
							if(board[i+1][y-1] != null)
								return false;
						}
						if(y < 9) {
							if(board[i+1][y+1] != null)
								return false;
						}
					}
				}
            }
    	}
    	return true;
    }



	public Ship[][] getBoard() {
		return board;
	}

	public void setBoard(Ship[][] board) {
		this.board = board;
	}
}
