# Java RMI Multi Client Whiteboard
A simple multi client whiteboard program, based on Java RMI. UI is implemented based on Java Swing UI.

# Features
- After the server starts, the manager can create a whiteboard and other users can join the newly created whiteboard room (If the manager have not created a room yet, an appropriate message will show on the user's side).
- The manager will receive a popup if any user wants to join in the whiteboard. The user need to wait the approval from the manager.
- Any drawing on one user's whiteboard will be syncronized to other users' whiteboards.
- Users can see the user list
- Users can chat with each other
- The manager can close the room. All users in the same room will be disconnected as well.
