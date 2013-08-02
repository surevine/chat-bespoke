<%@ page isErrorPage="true"%>
<%
	boolean handled = false; // Set to true after handling the error

	if(exception != null) {
		// We're handling a java exception
		handled = true;
%>
<h1>A server error has occurred</h1>
<p>If possible, you could try pressing the back button in your browser and attempt
the operation again</p>
<p>If that doesn't work then please make a note of what you were doing at the time and contact your system administrator.</p>
<p>We apologise for any inconvenience this may cause</p>
<%
	} else {
	
	    // Get the PageContext
	    if(pageContext != null) {
	    
	        // Get the ErrorData
	        ErrorData ed = null;
	        try {
	            ed = pageContext.getErrorData();
	        } catch(NullPointerException ne) {
	            // If the error page was accessed directly, a NullPointerException
	            // is thrown at (PageContext.java:514).
	            // Catch and ignore it... it effectively means we can't use the ErrorData
	        }
	
	        // Display error details for the user
	        if(ed != null) {
	            // Output details about the HTTP error
	            // (this should show error code 404, and the name of the missing page)
	            %>
	            <h1>Error Code <%= ed.getStatusCode() %></h1>
	            <% if(ed.getStatusCode() == 404) { %>
	            <p>The resource you requested cannot be found.</p>
	            <p>If you entered the address manually then you may have entered it incorrectly.</p>
	            <p>If you followed a link then the link may be out of date.</p>
	            <p>If you continue to encounter problems then please contact your system administrator.</p>
	            <% } else { %>
				<p>You could try pressing the back button in your browser and attempt
				the operation again.</p>
				<p>If that doesn't work then please make a note of what you were doing at the time and contact your system administrator.</p>
				<p>We apologise for any inconvenience this may cause.</p>
				<% } %>
	            <p>ErrorCode: <%= ed.getStatusCode() %><br />
	            URL: <%= ed.getRequestURI() %></p>
	            <%

	            
	            // Error handled successfully, set a flag
	            handled = true;
	        }
	    }
	}
    // Check if the error was handled
    if(!handled) {
    %>
<h1>An Unknown Error Occurred</h1>
<p>Please make a note of what you were doing at the time and contact your system administrator.</p>
<p>We apologise for any inconvenience this may cause</p>
	<%
    }
%>