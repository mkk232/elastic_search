<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Bootstrap Example</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
<script
	src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.slim.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script defer src="/js/search.js"></script>
</head>
<style>
span {
	margin-right: 2rem;
}
</style>


<body style="position: relative;">
	<input id="page" type="hidden" value="${pagination.page}">

		<table class="table table-bordered" style="width: 10%; position: fixed; left: 10%; top: 10%;">
			<thead id="clasificar_head">

			</thead>
			<tbody id="clasificar">
				
			</tbody>
		</table>
		
		<table class="table table-bordered" style="width: 10%; position: fixed; left: 10%; top: 26%;">
			<thead id="subclass_head">
			</thead>
			<tbody id="subclass">
				
			</tbody>
		</table>

	<div class="container sticky-top" style="background-color: #fff;">
		<div
			class="form-group d-flex justify-content-around align-items-center mt-4"
			style="height: 70px;">
			<input type="text" class="form-control w-75 h-100" id="keyword"
				name="keyword" style="font-size: 25px; padding: 0.5rem 2rem;"
				value="${keyword }" onKeyPress="if(event.keyCode == 13) search(1)">
			<button type="button" id="search" class="btn btn-primary p-4"
				onclick="search(1)">검색</button>
		</div>
	</div>
	<div id="contentWrap"></div>
	<footer class="container d-flex justify-content-center">
		<ul id="footer" class="pagination">

		</ul>
	</footer>
</body>
<style>
em.highlight {
	color: blue;
}
</style>
</html>
