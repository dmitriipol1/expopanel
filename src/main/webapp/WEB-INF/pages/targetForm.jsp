<%--
  Created by IntelliJ IDEA.
  User: Crazz
  Date: 15.06.2018
  Time: 11:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=2">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <title>Библиотека</title>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">

</head>
<body class=".container-fluid">
<div class="container myrow-container">
    <div class="panel panel-success">
        <div class="panel-heading">
            <h3 class="panel-title">
                <div align="left"><b>Источник контента</b></div>
                <div></div>
                <div align="right"><a href="/">Назад</a></div>
            </h3>
        </div>
        <div class="panel-body">
            <form:form id="bookRegisterForm" cssClass="form-horizontal" modelAttribute="target" method="post"
                       action="saveTarget">

                <div class="form-group">
                    <div class="control-label col-xs-3"><form:label path="name">Name</form:label></div>
                    <div class="col-xs-6">
                        <form:input cssClass="form-control" path="name"/>
                    </div>
                </div>

                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-4">
                        </div>
                        <div class="col-xs-4">
                            <input type="submit" id="saveTarget" class="btn btn-primary" value="Save"
                                   onclick="return submitBookForm();"/>
                        </div>
                        <div class="col-xs-4">
                        </div>
                    </div>
                </div>

            </form:form>
        </div>
    </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>


</body>
</html>

