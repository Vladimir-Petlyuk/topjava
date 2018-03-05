<%--
  Created by IntelliJ IDEA.
  User: Владимир
  Date: 05.03.2018
  Time: 7:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html;charset=cp1251"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=cp1251"/>
    <%--<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>--%>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1">

    <title>Edit Meal</title>
</head>
<body>

<form method="POST" action='meals' name="editMeal">

    User ID : <input type="text" readonly="readonly" name="mealId" value="<c:out value="${meal.id}"/>" required/> <br/>
    Description : <input type="text" name="description" value="<c:out value="${meal.description}" />"required/> <br/>
    Calories : <input type="text" name="calories" value="<c:out value="${meal.calories}" />" required/> <br/>
    DOC : <input type="datetime-local" name="doc" value="${meal.dateTime}" required/> <br/>
    <button type="submit">Submit</button>
</form>



</body>
</html>
