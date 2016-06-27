package student;

import java.util.ArrayList;
import java.util.List;

import danaus.*;

/**
 * This file is the only .java file you will be submitting to CMS. For javadoc
 * specifications, refer to AbstractButterfly. You do not need to copy the
 * javadocs into this file, but you may if you wish.
 */
public class Butterfly extends AbstractButterfly {
	private TileState[][] ts; // The array that learn() will create and fill.
	private List<Flower> flowerList;// This instantiates the flowerList.

	// move[][] is the numerical representation of a move, which corresponds to the directional
	// representation found in Dmove[].
	static private int[][] move={{1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1}};
	static private Direction[] Dmove={Direction.S,Direction.SE,Direction.E,Direction.NE,
		Direction.N,Direction.NW,Direction.W,Direction.SW};

	/** This implements learn(), which traverses the map entirely using a depth-first search
	 * (DFS). It returns a TileState[][] array, which contains the states of all the tiles
	 */
	public @Override TileState[][] learn() {
		if(ts == null){
			ts = new TileState[getMapHeight()][getMapWidth()];
			flowerList = new ArrayList<Flower>();
		}
		saveStateAndFlowers();
		for (int k = 0; k < 8; k++){
			int x = (move[k][1] + state.location.col)%getMapWidth();
			if (x < 0) x += getMapWidth();
			int y = (move[k][0] + state.location.row)%getMapHeight();
			if (y < 0) y += getMapHeight();
			if (ts[y][x] == null){
				try{
					fly(Dmove[k],danaus.Speed.FAST);
					learn();
					fly(Dmove[(k+4)%8],danaus.Speed.FAST);
					learn();
				} catch (danaus.CliffCollisionException | danaus.WaterCollisionException e) {}
			}
		}
		return ts;
	}

	/** This is a helper method for learn(), which finds Flower f and adds it to our flowerList
	 */
	private void saveStateAndFlowers(){
		refreshState();
		if (ts[state.location.row][state.location.col] == null){
			ts[state.location.row][state.location.col] = state;
			for (Flower f : state.getFlowers()){
				flowerList.add(f);
			}
		}
	}

	/** This method returns our flowerList
	 */
	public @Override List<Flower> flowerList() {
		return flowerList;
	}

	/** This method checks if Flower f is in our flowerList and returns its location
	 */
	public @Override Location flowerLocation(Flower f) {
		if (flowerList.contains(f)){
			return f.getLocation();
		}else{
			return null;
		}
	}

	public @Override void run(List<Flower> flowers) {
		// DO NOT IMPLEMENT
	}
}
