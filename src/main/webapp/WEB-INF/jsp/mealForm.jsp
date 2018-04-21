<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title><spring:message code="mealFrom.meal"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
<section>
    <h3><a href="${pageContext.request.contextPath}/meals/home"><spring:message code="mealFrom.home"/></a></h3>
    <c:choose>
        <c:when test="${action=='create'}">
            <h2><spring:message code="mealFrom.create"/></h2>
        </c:when>
        <c:otherwise>
            <h2><spring:message code="mealFrom.edit"/></h2>
        </c:otherwise>
    </c:choose>
    <hr>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <form method="post" action="${pageContext.request.contextPath}/meals/edit" >
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt><spring:message code="mealFrom.dateTime"/>:</dt>
            <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="meals.description"/>:</dt>
            <dd><input type="text" value="${meal.description}" size=40 name="description" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="meals.calories"/>:</dt>
            <dd><input type="number" value="${meal.calories}" name="calories" required></dd>
        </dl>
        <button type="submit"><spring:message code="mealFrom.save"/></button>
        <button onclick="window.history.back()" type="button"><spring:message code="mealFrom.cancel"/></button>
    </form>
</section>
</body>
</html>