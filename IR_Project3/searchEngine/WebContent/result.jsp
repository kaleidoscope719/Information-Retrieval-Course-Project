<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ page import="com.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>result</title>
</head>
<body>
<%  
	// get the input query
	String input1 = request.getParameter("query");
	String[] input=new String[2];
	input[0]=input1;
	List<Map.Entry<Integer,Double>> resultList = new ArrayList();
	if(indexSearch.main(input)!=null) {
		resultList = (ArrayList)indexSearch.main(input);
	} 
	
	//resultList = indexSearch.main(input);
	ArrayList<String> result = new ArrayList<String>();
	ArrayList<String> queries = new ArrayList<String>();
	ArrayList<String> title = new ArrayList<String>();	
	ArrayList<String> text = new ArrayList<String>();
	if(resultList.size()!=0){
		result=indexSearch.retrieveURL(resultList);
		title = indexSearch.retrieveTitle(resultList);
		text = indexSearch.retrieveText(resultList, input1);
	}	
	//request.setAttribute("result", result);
	//String google = "www.google.com";
	//out.println(input);
	// build doc id url map
	/* String[] resultUrl = new String[5];
	String[] resultTitle = new String[5];
	for(int i = 0; i < 5; i++) {
		resultUrl[i] = result.
		resultTitle[i] = docidTitleMap.get(result.get(0)[i].docid);
	} */
	
%> 

<%if(result.size() == 0) {%>
<p><%out.println("Sorry! Nothing found! :(");%></p>
<% }
%>
<%for(int i =0; i<result.size(); i++){
%>
<p>
<a href= "<%out.println(result.get(i));%>" ><%out.println(title.get(i)+"<br>");%></a>
<% out.println(text.get(i)); %><br />
</p><br />
<%
}
%>


<footer class="web-footer">
      <hr>
      <p class="text-center" align = "center">
        � 2015 Information Retrieval Project
      </p>
    </footer>
</body>
</html>