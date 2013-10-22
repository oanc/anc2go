class ANCTagLib {
   static namespace = 'anc'
   
   def home = { 
      out << "<span class='anc-home'><a href='/ANC2Go/application/index'>Home</a></span>"
   }
   
    def copyright = { 
       def year = Calendar.getInstance().get(Calendar.YEAR)
      out << "<div class='anc-copyright'>&copy; 2002 - ${year}, American National Corpus. All rights reserved.</div>"
   }
    
    def dojo = { atts ->
       def theme = atts.theme ?: 'tundra'
	   def file = "/dojo-1.9.1/dijit/themes/${theme}/${theme}.css"
	   //theme = "${resource(dir:'js', file:'/dojo-1.9.1/dijit/themes/${theme}/tundra.css') }"
	   theme = resource(dir:'js', file:file)
       out << """\
        <link rel="stylesheet" href="${resource(dir:'css',file:'anc.css')}" />
        <link rel="stylesheet" href="${theme}"/>       
        <link rel="stylesheet" href="${resource(dir:'js', file:'/dojo-1.9.1/dojo/resources/dojo.css') }"/>
        <link rel="shortcut icon" href="${resource(dir:'images', file:'favicon.ico')}" />
        <script type="text/javascript" src="/ANC2Go/js/dojo-1.9.1/dojo/dojo.js" djConfig="parseOnLoad:true"></script>
          """
   }
   
   def form = { atts, body ->
      out << "<div dojoType='dijit.form.Form' encType='multipart/form-data'"
      atts.each { key, value ->
         out << " ${key}=\"${value}\""
      }
      out << ">"
   	  out << body()
	  out << "</div>"  
   }
   
   def email = { atts ->
      out << "<input type='text' dojoType='dijit.form.ValidationTextBox'"
      out << " required='true'"
      out << ' regExp="[a-zA-Z0-9\\._+-]+@[a-zA-Z0-9\\.+-]+\\.[a-zA-Z0-9]{2,4}"'
      out << " promptMessage='Enter email address.'"
      out << " invalidMessage='Invalid Email Address.'"
      out << " trim='true'"
      atts.each { key, value ->
         out << " ${key}=\"${value}\""
      }
      out << "/>"
   }
   
   def hidden = { atts ->
	   out << "<input type='hidden' dojoType='dijit.form.TextBox'"
       atts.each { key, value ->
         out << " ${key}=\"${value}\""
      }
      out << "/>"
   }
   
   def checkbox = { atts ->
      def cssClass = 'anc-button'
      if (atts['class']) {
         cssClass = "anc-button ${atts['class']}"
         atts['class'] = null
      }
      out << "<input type='checkbox' dojoType='dijit.form.CheckBox' class='${cssClass}'"
      atts.each { key, value ->
         out << " ${key}=\"${value}\""
      }
      out << "/>"
   }
   
   def radio = { atts ->
      def cssClass = 'anc-button'
      if (atts['class']) {
         cssClass = "anc-button ${atts['class']}"
         atts['class'] = null
      }
      out << "<input type='radio' dojoType='dijit.form.RadioButton' class='${cssClass}'"
      atts.each { key, value ->
         out << " ${key}=\"${value}\""
      }
      out << "/>"
   }
   
   def textbox = { atts ->
      out << "<input type='text' dojoType='dijit.form.TextBox'"
      atts.each { key, value ->
         out << " ${key}=\"${value}\""
      }
      out << "/>"
   }
   
   def dialog = { atts, body ->
      out << "<div dojoType='dijit.Dialog' class='dialog'"
      atts.each { key, value ->
         out << " ${key}=\"${value}\""
      }
      out << ">${body()}</div>"
   }
   
    /** Wraps the body in a div tag with the proper attributes to 
     *  define the div as a Dojo BorderContainer.
     *  
     *  @attr id the id to be assigned to the div element
     *  @attr divClass the value of the class attribute on the div element.
     *  @attr the type of dojo div to create. Do no include the dijit.layout prefix.
     *  @attr design border container design. Defaults to "headline"
     */
   def panel = { atts, body ->
       out << "<div"
       out << " dojoType='dijit.layout.ContentPane'"
       atts.each { key, value ->
          out << " ${key}=\"${value}\""
       }
       out << ">${body()}</div>"
   }
   
   /**
    * Wraps the body in a div tag with the proper attributes to define the
    * div as a Dojo ContentPane.
    * 
    * @attr id the id to be assigned to the div element
    * @attr divClass the value of the class attribute assigned to the div
    * @attr props where the div will be placed in the border container.
    * 
    */
   def content = { atts, body ->
       out << "<div"
       out << " dojoType='dijit.layout.ContentPane'"
       atts.each { key, value ->
          out << " ${key}=\"${value}\""
       }
       out << ">${body()}</div>"
   }
   
   def border = { atts, body ->
       out << "<div"
       out << " dojoType='dijit.layout.BorderContainer'"
       atts.each { key, value ->
          out << " ${key}=\"${value}\""
       }
       out << ">${body()}</div>"
   }
   
   def tabbed = { atts, body ->
       out << "<div"
       out << " dojoType='dijit.layout.TabContainer'"
       atts.each { key, value ->
          out << " ${key}=\"${value}\""
       }
       out << ">${body()}</div>"
   }   
}
