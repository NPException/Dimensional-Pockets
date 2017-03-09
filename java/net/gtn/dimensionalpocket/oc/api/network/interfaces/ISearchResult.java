package net.gtn.dimensionalpocket.oc.api.network.interfaces;

import java.util.List;

public interface ISearchResult {

    /**
     * @return true if the search has successfully discovered a path.
     */
    public boolean hasFinished();

    /**
     * @return the path that was discovered, else Collections.<INetworkNode>emptyList() (This could be returned either because the path doesn't exist OR because it's not yet finished.).
     */
    public List<INetworkNode> getPath();

}
