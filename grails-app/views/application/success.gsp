<%--
  Created by IntelliJ IDEA.
  User: Tim
  Date: 5/27/15
  Time: 5:34 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
	<title>ANC2Go Multi</title>
	<script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href=<g:resource dir="css" file="anc2go.css" /> />
<link rel="stylesheet" type="text/css" href=<g:resource dir="css" file="bootstrap.min.css" /> />
<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
</head>
<>
<%-- menu code --%>
<div id="nav">
    <div id="nav_content">
        <a href="http://www.anc.org" id="logo"></a>
        <div class="menu">
            <ul>
                <li><a href="http://www.anc.org/" alt="Home">home</a></li>
                <li class=""><a href="http://www.anc.org/about/" alt="About the ANC Project">about</a>
                    <ul class='children'>
                        <li class=""><a href="http://www.anc.org/about/people/" alt="People">people</a></li>
                        <li class=""><a href="http://www.anc.org/about/contact/" alt="Contact Us">contact</a></li>
                        <li class=""><a href="http://www.anc.org/about/updates/" alt="Updates">updates</a></li>
                        <li class=""><a href="http://www.anc.org/about/publications/" alt="Publications">publications</a></li>
                        <li class=""><a href="http://www.anc.org/about/anc-in-the-news/" alt="anc in the news">anc in the news</a></li>
                    </ul>
                </li>
                <li class=""><a href="http://www.anc.org/data/" alt="data">data</a>
                    <ul class='children'>
                        <li class=""><a href="http://www.anc.org/data/oanc/" alt="Open ANC">oanc</a></li>
                        <li class=""><a href="http://www.anc.org/data/masc/" alt="MASC">masc</a></li>
                        <li class=""><a href="http://www.anc.org/data/anc-second-release/" alt="ANC Second Release">second release</a></li>
                    </ul>
                </li>
                <li class=""><a href="http://www.anc.org/contribute/" alt="contribute">contribute</a>
                    <ul class='children'>
                        <li class=""><a href="http://www.anc.org/contribute/texts/" alt="Contribute Texts">texts</a></li>
                        <li class=""><a href="http://www.anc.org/contribute/contribute/" alt="Contribute Annotations">data and annotations</a></li>
                    </ul>
                </li>
                <li class="current-page-ancestor current-page-parent"><a href="http://www.anc.org/software/" alt="tools">tools</a>
                    <ul class='children'>
                        <li class=""><a href="http://www.anc.org/software/anc-tool/" alt="ANC Tool">ANC Tool</a></li>
                        <li class="current-page-item"><a href="http://www.anc.org/software/anc2go/" alt="ANC2Go">ANC2Go</a></li>
                        <li class=""><a href="http://www.anc.org/software/gate-tools/" alt="GATE Tools">GATE Tools</a></li>
                        <li class=""><a href="http://www.anc.org/software/uimautils/" alt="UIMA Tools">UIMA Tools</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</div>
    <wp:frame>
    <div id="subnav">
        <ul>
            <li class=""><a href="http://www.anc.org/software/anc-tool/" alt="ANC Tool">ANC Tool</a></li>
            <li class="current-page-item"><a href="http://www.anc.org/software/anc2go/" alt="ANC2Go">ANC2Go</a></li>
            <li class=""><a href="http://www.anc.org/software/gate-tools/" alt="GATE Tools">GATE Tools</a></li>
            <li class=""><a href="http://www.anc.org/software/uimautils/" alt="UIMA Tools">UIMA Tools</a></li>
        </ul>
    </div>
    <wp:wrapper>
        <!--
        <wp:section title="Success">
            Your request was successfully submitted. You will receive an email shortly with a link to your download.
        </wp:section>
        -->
        <wp:section title="Success">
            <p>Your request was successfully submitted.</p>
            <p>Unfortunately, sending email is currently disabled so you will not be notified when
            your files are ready. Please allow at least five minutes for processing to complete and your files
            should be available from
            <a href="http://www.anc.org/downloads/ANC2Go/${filename}">http://www.anc.org/downloads/ANC2Go/${filename}</a>.
            </p>
            <p>If the above link is not working within one hour please send an email to <span class="label">anc [at] anc.org</span>.</p>
            <p><button onclick="window.history.back()">Back</button></p>
        </wp:section>
    </wp:wrapper>
    </wp:frame>
    <script language="javascript" type="text/javascript" src=<g:resource dir="js" file="anc2go.js" /> ></script>
    </body>
</html>