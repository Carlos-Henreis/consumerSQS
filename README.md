# consumerSQS

Cabeçalho Angular com Voxel.


User
Criar uma tela com header azul com título clicável na esquerda e na direita nome usuário e botão de sair angular com voxel component

ChatGPT
Claro! Aqui está um exemplo de como você pode criar uma tela com um cabeçalho azul no Angular, com um título clicável à esquerda, o nome do usuário à direita e um botão de sair usando o componente Voxel:

Primeiro, vamos criar o componente para o cabeçalho chamado "HeaderComponent":

header.component.html:

html
Copy code
<header class="header">
  <div class="title" (click)="handleTitleClick()">Título Clicável</div>
  <div class="user-info">
    <span>{{ username }}</span>
    <button class="exit-button" (click)="handleExitClick()">Sair</button>
  </div>
</header>
header.component.ts:

typescript
Copy code
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  @Input() username: string;

  handleTitleClick() {
    // Lógica para lidar com o clique no título
  }

  handleExitClick() {
    // Lógica para lidar com o clique no botão de sair
  }
}
header.component.css:

css
Copy code
.header {
  background-color: blue;
  color: white;
  display: flex;
  justify-content: space-between;
  padding: 10px;
}

.title {
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
}

.exit-button {
  margin-left: 10px;
}
Agora, você pode usar o componente HeaderComponent em qualquer outro componente Angular da seguinte maneira:

outro-componente.component.html:

html
Copy code
<app-header [username]="nomeUsuario"></app-header>
outro-componente.component.ts:

typescript
Copy code
import { Component } from '@angular/core';

@Component({
  selector: 'app-outro-componente',
  templateUrl: './outro-componente.component.html',
  styleUrls: ['./outro-componente.component.css']
})
export class OutroComponenteComponent {
  nomeUsuario = 'João';
}
Certifique-se de ter instalado o Angular e o componente Voxel corretamente em seu projeto.
