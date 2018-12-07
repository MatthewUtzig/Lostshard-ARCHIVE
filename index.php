<html>
<head>
	<link rel="stylesheet" href="css/stylesheet.css" type="text/css" charset="utf-8"/>
	<link rel="stylesheet" href="css/nivo-slider.css" type="text/css" media="screen"/>
	<script src="js/jquery1.4.js" type="text/javascript"></script>
	<script src="js/jquery.nivo.slider.pack.js" type="text/javascript"></script>
	<script type="text/javascript">
	$(window).load(function() {
		$('#slider').nivoSlider({
			effect:'fade', //Specify sets like: 'fold,fade,sliceDown'
			slices:15,
			animSpeed:500, //Slide transition speed
			pauseTime:4000,
			startSlide:0, //Set starting Slide (0 index)
			directionNav:false, //Next & Prev
			directionNavHide:false, //Only show on hover
			controlNav:false, //1,2,3...
			controlNavThumbs:false, //Use thumbnails for Control Nav
			controlNavThumbsFromRel:false, //Use image rel for thumbs
			controlNavThumbsSearch: '.gif', //Replace this with...
			controlNavThumbsReplace: '_thumb.jpg', //...this in thumb Image src
			keyboardNav:true, //Use left & right arrows
			pauseOnHover:false, //Stop animation while hovering
			manualAdvance:false, //Force manual transitions
			captionOpacity:0.8, //Universal caption opacity
			beforeChange: function(){},
			afterChange: function(){},
			slideshowEnd: function(){}, //Triggers after all slides have been shown
			lastSlide: function(){}, //Triggers when last slide is shown
			afterLoad: function(){} //Triggers when slider has loaded
		});
	});
	</script>
</head>
<body>
<div id="container">
	<div id="content">
		<div id="header">
			Lost Shard
		</div>

		<div id="nav">
		<a href="index.php" style="text-decoration:underline;">Home</a>, <a href="/wiki/wiki.php">Guide</a>, <a href="/blog/blog.php">Updates</a>, <a href="/server/server.php">Server Info</a>, <a href="/forums/forums.php">Forum</a>, <a href="/subscription/subscription.php">Subscription</a>
		</div>
		</br>
		</br>
		<div id="slider">
			<!--<img src="images/ss1.gif" alt="" />
			<img src="images/ss2.gif" alt="" />
			<img src="images/ss3.gif" alt="" />-->
			<img src="images/image1.png" alt=""/>
			<img src="images/image2.png" alt=""/>
			<img src="images/image3.png" alt=""/>
			<img src="images/image4.png" alt=""/>
			<img src="images/image5.png" alt=""/>
			<img src="images/image6.png" alt=""/>
			<img src="images/image7.png" alt=""/>
			<img src="images/image8.png" alt=""/>
			<img src="images/image9.png" alt=""/>
			<img src="images/image10.png" alt=""/>
		</div>

		<div id="about">
		</br>
		Lost Shard is an RPG server for minecraft. It features, among other things, a gold based currency, a system of land ownership and skills and abilities that allow you to differentiate yourself from everyone else.</br>
		</br>
		</div>
	</div>
</div>
</body>
</html>
