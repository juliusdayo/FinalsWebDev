<%-- 
    Document   : register_user
    Created on : 06 23, 21, 10:52:52 AM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:include page="../templates/header.jsp" />            
<jsp:include page="../templates/sidebar.jsp"/>


<div class="col-md-7">         
    <div class="card card-primary">
        <div class="card-header">
            <h3 class="card-title">Add User</h3>
        </div>
        <!-- /.card-header -->
        <!-- form start -->
        <div class="card-body">
            <form role="form" action="${pageContext.request.contextPath}/RegisterUser" method="post">
                <div class="row">
                    <div class="form-group col-sm-4">
                        <label>First Name</label>
                        <input class="form-control" type="text" id="first_name" name="first_name">
                    </div>
                    <div class="form-group col-sm-4">
                        <label>Middle Name</label>
                        <input class="form-control" type="text" id="middle_name" name="middle_name">
                    </div>
                    <div class="form-group col-sm-4">
                        <label>Last Name</label>
                        <input class="form-control" type="text" id="last_name" name="last_name">
                    </div>
                </div>
                <div class="row">
                    <div class="form-group col-sm-6">

                        <label for="username">Username</label>
                        <input class="form-control" type="text" id="username" name="username">
                    </div>
                    <div class="form-group col-sm-6">
                        <label>Password</label>
                        <input class="form-control" type="password" id="password" name="password">
                    </div>

                </div>
                <div class="row">
                    <div class="form-group col-sm-6">
                        <label>User Role</label>
                        <select class="form-control" id="role" name="role">
                            <option value="1">Manager</option>
                            <option value="2">Staff</option>
                        </select>
                    </div>
                </div>

                <div class="card-footer">
                    <button class="btn btn-primary">Add</button>
                </div>
            </form>
        </div>
    </div>

</div>

<jsp:include page="../templates/footer.jsp"/>