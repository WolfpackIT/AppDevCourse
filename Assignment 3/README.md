# Assignment 3: Adding tabs
Continue building on what you have made in Assignment 2

## Reading materials
- User interface and & navigation
  - Layouts --> create a list with recyclerview (usage of adapters)
  - Look and feel  
    - In depth: styles and themes, floating action button buttons
    - Quickly checkout: checkboxes, radio buttons, toggle,spinners. 
  - Notifications
     - Create a notification 
     - *other topics not needed*
  - Slide between fragments with the viewpager
- Background tasks
  - Background operations overview
  - Specifiying the code to run on a thread
  - *other topics not needed*
- Connectivity
  - Transmitting Network data using volley
    - Sending a simple Request
    - Additional: https://www.itsalif.info/content/android-volley-tutorial-http-get-post-put
    - *other topics not needed*

## New app components
- Tab infrastructure. 
  - One tab points to what was MainAcitivy and is called 'Contact Wolfpack' (convert to Fragment and make new MainActivity) [viewpager]
  - Second tab is called 'Shout!'. This Fragment shows a List of possible shoutouts that can be posted to our Slack channel.  [recyclerview] 

## New app requirements
- When clicking on one of the Shout List items, a background task ensures the shout is send through the API of Slack and posted in our random channel [background tasks]
- Inform the user using toasts of succes/fail of this task
- Sending the message to the slack API is a simple POST call implementation [connectivity]
- Choose your own shouts like 'I want coffee NOW'
- When email is succesfully sent (check the callback by the email intent) A notification (in the notificationbar) has to be posted saying 'Sent an picture to Rene!' [notifications]
- Change the colorscheme of the app to your choice BUT using the correct styling and themeing options.
- Use the debugger to place breakpoints when testing the app. play around with it!
- Don't forget the appropriate permiissions

**Done when I receive an email and post displays in Slack**

