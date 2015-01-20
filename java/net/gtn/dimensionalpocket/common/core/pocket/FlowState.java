package net.gtn.dimensionalpocket.common.core.pocket;

import java.awt.Color;

public enum FlowState {
    NONE("#646464", 128),
    ENERGY_INPUT("#FFD83B", 255),
    ENERGY_OUTPUT("#EE70FF", 255);
    
    public int r,g,b,a;
    private FlowState(String rgb, int a) {
    	Color color = Color.decode(rgb);
		this.r = color.getRed();
		this.g = color.getGreen();
		this.b = color.getBlue();
		this.a = a;
	}
}
