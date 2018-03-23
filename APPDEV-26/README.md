# Assignment 4: Add Geofencing
Continue building on what you have made in APPDEV-19

## Reading materials
- User interface and & navigation
  - Notifications
    - Create an exandable notification
    - Start an activity from a notification
    - Modify a notification badge
  - Adding the appBar
  - Designing effective navigation (planning screens,providing descendant and lateral navigation, providing ancestral and temporal navigation)
- User location
  - Getting the last known location
  - Changing location updates
  - Receiving location updates
  - Creating and monitoring geofences
- Touch and input
  - Input events
  - Using touch gestures (detecting common, tracking touch and pointer, not the rest)

## New app components
- New tab 'geofencing' with new fragment
- Geofencing the office: The app has to show a notification, everytime you enter the proximity of the office.
- Usage of appbar

## New app requirements
- The geofence tab shows Appbar actions 'set fence' and 'get location'. Furthermore the tab has a recyclerview showing all current set fences.  [reclyclerview] [appbar] 
- When you click set-fence you register a geofence on the current location. Handle callbacks and errors correctly, also inform the user about succes/failure.  Add the set geofence as a reclyclerview listitem to the list. 
- When you click get-fence you get a notification informing you about the current location. (exactly the same notification as next bullet)
- The app AUTOMATICALLY shows a notification 'welcome @ Woflpack' every time you enter the proximity of the office. based on GPS geofencing  [notifications][sensors]. Make smart decisions regarding battery usage and accuracy. 
- The notification should be expandable, showing as much as possible Geofence output (long,lat, accuracy etc).[notifications]
- The notification is clickable, one button 'start app' starting the app and sending you to the 'shout' part of the app. (use correct navigation) and one button 'archive' dismissing the app [notifications]
- Use the debugger to place breakpoints when testing the app. play around with it!
- Make sure you save all preferences and settings using shared preference. Usability is important
- Don't forget the appropriate permissions

**Show René when done**
