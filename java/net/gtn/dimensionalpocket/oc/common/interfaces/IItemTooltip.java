package net.gtn.dimensionalpocket.oc.common.interfaces;

import java.util.Collection;
import java.util.List;

public interface IItemTooltip {

    /**
     * Used to actually add the information to a list.
     * @param list - the list to populate.
     */
    public void populateList(List<String> list);

    /**
     * Used to set a default non-shift list.
     * Such as: "Press SHIFT for more." 
     */
    public void defaultInfoList();

    /**
     * Adds the text to both the information list, and the shift list.  
     * @param string - String to add
     */
    public void addToBothLists(String string);

    /**
     * Adds the text to the information list. 
     * @param string - String to add
     */
    public void addToInfoList(String string);

    /**
     * Add the collection of strings to add to the information list.
     * @param strings - Strings to add
     */
    public void addAllToInfoList(String... strings);

    /**
     * Add the collection of strings to add to the information list.
     * @param strings - Strings to add
     */
    public void addAllToInfoList(Collection<String> strings);

    /**
     * Adds the text to add to the shift list.
     * @param string - String to add
     */
    public void addToShiftList(String string);

    /**
     * Add the collection of strings to add to the shift list.
     * @param strings - Strings to add
     */
    public void addAllToShiftList(String... strings);

    /**
     * Add the collection of strings to add to the shift list.
     * @param strings - Strings to add
     */
    public void addAllToShiftList(Collection<String> strings);
}
