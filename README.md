# 🌟 Novidade Incrível: Geração de PDF Acessível! 🌟

**Agora é fácil e inclusivo!** ✨
aws dynamodb create-table --cli-input-json file://cupo_desc.json --endpoint-url=http://localhost:4566
## O que há de novo?
- Introduzimos a tão aguardada funcionalidade de **geração de PDF acessível** em nossa API.
- Crie PDFs acessíveis em poucos cliques e torne seus documentos disponíveis para todos os usuários, independentemente de suas necessidades de acessibilidade.

## Principais benefícios:
- 📃 PDFs inclusivos: Gere documentos que atendem aos padrões de acessibilidade, tornando-os legíveis e compreensíveis para todos.
- 🎨 Personalização fácil: Mantenha a identidade visual do seu template HTML ao gerar PDFs acessíveis.
- 🚀 Desenvolvimento ágil: Aproveite nossa API de fácil integração e implemente a funcionalidade em questão de minutos.

## Destaque: Padrão WCAG 2.1
Nossa nova funcionalidade segue rigorosamente o padrão de acessibilidade **WCAG 2.1**, garantindo que seu conteúdo seja acessível para pessoas com deficiências visuais, auditivas e cognitivas.

## Veja como funciona:

![Exemplo de Geração de PDF Acessível](imagem_exemplo.png)

## Chame para ação:
Experimente agora mesmo nossa nova funcionalidade de geração de PDF acessível e aprimore a experiência dos seus usuários!

**Disponível na versão 2.5.0 do nosso aplicativo/API.**

Agradecemos a todos os nossos usuários pelo feedback contínuo, que nos ajudou a tornar esta funcionalidade uma realidade. Continuamos comprometidos em fornecer soluções inovadoras e acessíveis para você.

Atualize agora e aproveite a novidade! 🚀




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
