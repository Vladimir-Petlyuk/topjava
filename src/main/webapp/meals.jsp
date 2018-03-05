<%--
  Created by IntelliJ IDEA.
  User: Владимир
  Date: 04.03.2018
  Time: 17:10
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1">
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <jsp:useBean id="listMeals" scope="request" type="java.util.List"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <title>Список еды</title>
</head>
<body>
<div class="container">
    <table class="table table-striped">
        <thead>
        <tr>
            <th>Date of creation</th>
            <th>Description</th>
            <th>Calories</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${listMeals}" var="meal">
            <tr id="${meal.id}" style="background-color: ${meal.exceed ? 'pink' : 'white'}">
                <td>
                        ${meal.dateTime.format(formatter)}
                </td>
                <td>
                        ${meal.description}
                </td>
                <td>
                        ${meal.calories}
                </td>
                <td><a href="meals?action=edit&mealId=<c:out value="${meal.id}"/>">Update</a>
                    <a href="meals?action=delete&mealId=<c:out value="${meal.id}"/>">Delete</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <p><a href="meals?action=insert">Add Meal</a></p>
</div>


</body>
</html>
