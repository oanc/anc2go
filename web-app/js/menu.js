// Copyright 2005 (c) The American National Corpus.  All rights reserverd.
// Permission is granted to redistribute this file, in modified or unmodified form, as
// long as the above copyright notice and this permission statement are included.
// Author: Keith Suderman (suderman@cs.vassar.edu)

// The "current" top level menu item the user has selected, if any.
var navMenu = null;

// The previous top level menu item.
var prevNavMenu = null;

// The previous drop down menu
var prevDropMenu = null;

// This should be set in the CSS style sheet...
// And now is... these are ignored.
var bgLinkColor = '#999966';
var bgLinkHover = '#666633';
var bgLinkActive = '#000000';
var linkColor = '#000000';
var linkHover = '#2F4F4F';
var linkActive = '#778899';

// Various browser flags
var isIE = false;
var isSafari = false;

var dx = 0;
var dy = 0;

// Setting document.onclick during an onclick event causes the document.onclick
// event to be fired immediately.  This flag is used to tell our onclick handler
// to ignore the event.
var ignoreClick = false;


// Some elementary browser sniffing
if (navigator.appName.indexOf('Microsoft Internet Explorer') != -1)
{
	isIE = true;
	//dx = 5;
	//dy = -20;
}

if (navigator.appVersion.indexOf('Safari') != -1)
{
   isSafari = true;
   //dx = 50;
   //dy = 20;
}
  
// A shortcut for document.getElementById, which is way too much
// typing... 
// This should be expanded to be a little more browser friendly.
function get(id)
{
	return document.getElementById(id);
}

// Initialize the drop down menus.  This is called by the onload
// method of the <body> element.
function initDropMenu()
{
	var menuBar = get('menu-bar');
	var links = menuBar.getElementsByTagName('a');
	var i;
	for (i = 0; i < links.length; ++i)
	{
		var menu = links.item(i);
		if (menu.id)
		{
			initMenu(menu.id);
		}
	}
	
	// If this is the upload page we need to initialize a few
	// drop-down form elements.  The non_fiction() function is 
	// in subdomain.js and is only included in the upload page.
	var upload = get('upload-page');
	if (upload != null)
	{
		non_fiction();
	}
	navMenu = null;
}

function windowStatus(s)
{
	var p = get("status-bar");
	if (p)
	{
		p.firstChild.nodeValue = 'Status : ' + s;
	}
}


function initMenu(menuName)
{
	navMenu = get(menuName);
	navMenu.onclick = showDropMenu;
	var dm = get('drop-' + menuName);
	if (dm)
	{
		// see if there is a width specified for this menu.
		var width = get('drop-' + menuName + '-width');
		if (width)
		{
			//alert('setting menu width to ' + width.firstChild.nodeValue);
			dm.style.width = width.firstChild.nodeValue;
		}			
	}
	//navMenu.onmouseover = menuHover;
	//navMenu.onmouseout = menuOut;

	//dropName = 'drop-' + menuName;
	//dropMenu = get(dropName);
	//dropMenu.style.visibility = 'hidden';		
}

// Event called when the user clicks on an item in the
// top menu bar.
function showDropMenu(e)
{
	// The drop down menu to display
	menuName = 'drop-' + this.id;
	dropMenu = get(menuName);
	
	// If there is already a drop menu showing we will need to hide it.
	// Also make sure we don't hide a menu we are being asked to show
	if (prevDropMenu && prevDropMenu != dropMenu)
		hideDropMenu();
		
	// The top menu item
	navMenu = get(this.id);
	if (dropMenu)
	{
		// Find the bottom left of the parent menu item
		xPos = navMenu.offsetParent.offsetLeft + navMenu.offsetLeft;
		yPos = navMenu.offsetTop + navMenu.offsetHeight;
		// Adjust for browsers
		xPos += dx;
		yPos += dy;
		//if (isSafari)
		//{
		//	yPos += navMenu.offsetParent.offsetTop;
		//}
		// Position the menu and display it.
		dropMenu.style.left = xPos + 'px';
		dropMenu.style.top = yPos + 'px';
		dropMenu.style.visibility = 'visible';

		// Keep track of the current menu item.
		prevDropMenu = dropMenu;
		prevNavMenu = navMenu;

		// Set the ignoreClick flag.  Since we add an onclick handler it seems
		// it is going to get called to handle this click!
		ignoreClick = true;
		document.onclick = hideDropMenu;
	}

}

// Handler called to hide the drop down menu being displayed.
function hideDropMenu(e)
{
	if (ignoreClick)
	{
		ignoreClick = false;
		return;
	}
	document.onclick = null;
	if (prevDropMenu) // && prevDropMenu != dropMenu)
	{
		prevDropMenu.style.visibility = 'hidden';
		prevDropMenu = null;
	}
}

function focus()
{
	if (this.blur)
		this.blur();
}

