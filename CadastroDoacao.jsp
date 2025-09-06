<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Cadastro de Doação</title>
    <link rel="stylesheet" href="CadastroDoacao.css">
</head>
<body>
<div class="container">
    <h2>Cadastro de Doação</h2>
    <form action="CadastroDoacaoServlet" method="post" enctype="multipart/form-data">

        <!-- Dados do usuário -->
        <div class="form-group">
            <label>Nome:</label>
            <input type="text" name="nome" value="${nome}" readonly>
        </div>
        <div class="form-group">
            <label>Sobrenome:</label>
            <input type="text" name="sobrenome" value="${sobrenome}" readonly>
        </div>
        <div class="form-group">
            <label>Endereço:</label>
            <input type="text" name="endereco" value="${endereco}" readonly>
        </div>
        <div class="form-group">
            <label>Telefone:</label>
            <input type="text" name="telefone" value="${telefone}" readonly>
        </div>

        <!-- Dados da doação -->
        <div class="form-group">
            <label>Descrição:</label>
            <textarea name="descricao" required></textarea>
        </div>
        <div class="form-group">
            <label>Tipo:</label>
            <select name="id_tipo" required>
                <option value="">Selecione o tipo</option>
                <c:forEach var="tipo" items="${tipos}">
                    <option value="${tipo.id}">${tipo.descricao}</option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label>Status:</label>
            <input type="text" name="status" value="Disponível" readonly>
        </div>
        <div class="form-group">
            <label>Foto:</label>
            <input type="file" name="foto" accept="image/*">
        </div>

        <button type="submit">Cadastrar Doação</button>
    </form>
</div>
</body>
</html>
