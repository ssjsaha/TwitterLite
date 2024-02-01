# TwitterLite
Grettings!
Here is a lite version of twitter app.
Features:
1.Login
2.Signup
3.Add post with image and text or only text  (Only image from camera is implemented)  
logout

Features wanted to add  if given more time
1. Add animation
2. Add token base auth
3. add gallery image flexibility
4. add more test cases

Known issue:
Image being rotated when getting back from server

solution:
Currently, base64 encoding of image is used to show and upload since no strong backend is supported. If got image url from backend,then could use image library for showing image as well that would have taken care of the rotation 


How to run the app:
Simply clone the repo and run the app in android studio
