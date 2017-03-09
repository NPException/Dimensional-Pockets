package net.gtn.dimensionalpocket.oc.api.network.interfaces;

public interface ISearchPattern extends ISearchResult {

    /**
     * @return true if path was found.
     */
    public boolean searchForPath();

    /**
     * Used by the search thread to determine if the pattern can be deleted.
     * @return true if it can be deleted.
     */
    public boolean canDelete();
    
}
