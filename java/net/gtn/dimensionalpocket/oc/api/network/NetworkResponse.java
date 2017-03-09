package net.gtn.dimensionalpocket.oc.api.network;

public class NetworkResponse {

    //@formatter:off
    /**
     * PRE_PROCESSING:
     *  INVALID:
     *      Removes message from the system, and stops being processed.
     *  VALID:
     *      Propagates the message up to the next phase.
     *  WAIT:
     *      Leaves the message as is, calls upon it the next tick.
     *      (Insanely useful for waiting for a ISearchResult to complete.)
     *
     * PROCESSING
     *  INVALID:
     *      Stops propagating down that branch.
     *      (Useful if you're following a path.)
     *  VALID:
     *      Continues propagating the message down that path.
     *  WAIT:
     *      Stops the propagation and moves to the next message.
     *      (You shouldn't respond with this, as this is during mid-propagation, so you're costing CPU time. You should have setup all the data you want beforehand.)
     *
     * POST_PROCESSING
     *  INVALID:
     *      This is viewed as the message needs to be reposted, so pushes it back to the first phase.
     *      resetMessage() is also called if you desire to reset any data, or set a flag, etc.
     *  VALID:
     *      The message has finished/completed its job.
     *      The system removes it from the messaging system.
     *  WAIT:
     *      The message is waiting for something.
     *      Continues to the next message, leaving this message for the next tick.
     *      (Useful for setting up new messages, but have to wait for a path from node A to node B)
     */
    //@formatter:on
    public static enum MessageResponse {
        INVALID,
        VALID,
        WAIT;
    }

    /**
     * These responses are designed to help you identify what the network is doing.
     * NETWORK_JOIN:
     *  A single adjacent network has been found and been added.
     * NETWORK_MERGE:
     *  More than one valid network has been found, and these networks have been merged.
     * NETWORK_CREATION:
     *  No valid adjacent network has been found, so one was created.
     */
    public static enum NodeAdded {
        NETWORK_JOIN,
        NETWORK_MERGE,
        NETWORK_CREATION,

        // Not used internally. Just here to for you to replace the response if an exception is thrown.
        NETWORK_FAILED_TO_ADD;
    }

    public static enum NodeRemoved {
        NETWORK_NOT_FOUND,
        NETWORK_LEAVE,
        NETWORK_SPLIT,
        NETWORK_DESTROYED,

        // Not used internally. Just here to for you to replace the response if an exception is thrown.
        NETWORK_FAILED_TO_REMOVE;
    }

    public static enum NodeUpdated {
        NETWORK_NOT_FOUND,
        NETWORK_NO_DELTA_DETECTED,
        NETWORK_UPDATED,

        // Not used internally. Just here to for you to replace the response if an exception is thrown.
        NETWORK_FAILED_TO_UPDATE;
    }

    //@formatter:off
    /**
     * IGNORE:
     *  The node did nothing to the message.
     * INTERCEPT:
     *  The node claims it as it's own.
     *  As such, the node gets set as it's owner.
     *  And the message gets notified of dataChanged.
     * INJECT:
     *  The node has added/removed/edited data in some way in or through the message.
     *  The message gets notified of this.
     * DELETE:
     *  The message is deleted from the network, and unable to continue propagating.
     */
    //@formatter:on
    public static enum ListenerResponse {
        IGNORE,
        INTERCEPT,
        INJECT,
        DELETE;
    }
}
