let stompClient = null;
let currentUser = 'User_' + Math.floor(Math.random() * 1000);

function connect() {
    const socket = new SockJS('http://127.0.0.1:8080/ws');
    stompClient = Stomp.over(socket);
    
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        
        // Subscribe to messages
        stompClient.subscribe('/topic/public', function(message) {
            showMessage(JSON.parse(message.body));
        });
        
        // Announce join
        stompClient.send("/app/chat.addUser", {}, JSON.stringify({
            sender: currentUser,
            type: 'JOIN'
        }));
    });
}

function sendMessage() {
    const messageInput = document.getElementById('message-input');
    const content = messageInput.value.trim();
    
    if(content && stompClient) {
        const chatMessage = {
            sender: currentUser,
            content: content,
            type: 'CHAT'
        };
        
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
}

function sendFile() {
    const fileInput = document.getElementById('file-input');
    const file = fileInput.files[0];
    
    if(!file) {
        console.log('No file selected');
        return;
    }
    
    console.log('Reading file:', file.name, 'Size:', file.size);
    
    if(stompClient) {
        const reader = new FileReader();
        
        reader.onload = function(e) {
            console.log('File read complete');
            
            // Get base64 data (remove data:*/*;base64, prefix)
            const base64Data = e.target.result.split(',')[1];
            
            console.log('Base64 data length:', base64Data ? base64Data.length : 0);
            
            const chatMessage = {
                sender: currentUser,
                content: `Sent file: ${file.name}`,
                type: 'FILE',
                fileData: {
                    fileName: file.name,
                    fileType: file.type,
                    fileSize: file.size,
                    base64Data: base64Data
                }
            };
            
            console.log('Sending message:', chatMessage);
            stompClient.send("/app/chat.sendFile", {}, JSON.stringify(chatMessage));
            fileInput.value = '';
        };
        
        reader.onerror = function(e) {
            console.error('Error reading file:', e);
        };
        
        reader.readAsDataURL(file);
    } else {
        console.error('WebSocket not connected');
    }
}

function showMessage(message) {
    const messagesDiv = document.getElementById('messages');
    const messageElement = document.createElement('div');
    
    if(message.type === 'FILE') {
        const fileData = message.fileData;
        
        if(fileData.base64Data) {
            // Small file - create download link from base64
            const blob = base64ToBlob(fileData.base64Data, fileData.fileType);
            const url = URL.createObjectURL(blob);
            messageElement.innerHTML = `
                <strong>${message.sender}:</strong> 
                <a href="${url}" download="${fileData.fileName}">
                    📎 ${fileData.fileName} (${formatFileSize(fileData.fileSize)})
                </a>
            `;
        } else {
            // Large file - use server URL
            messageElement.innerHTML = `
                <strong>${message.sender}:</strong> 
                <a href="${fileData.fileUrl}" download>
                    📎 ${fileData.fileName} (${formatFileSize(fileData.fileSize)})
                </a>
            `;
        }
    } else if(message.type === 'CHAT') {
        messageElement.innerHTML = `<strong>${message.sender}:</strong> ${message.content}`;
    } else {
        messageElement.innerHTML = `<em>${message.sender} ${message.type === 'JOIN' ? 'joined' : 'left'}</em>`;
    }
    
    messagesDiv.appendChild(messageElement);
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}

function base64ToBlob(base64, contentType) {
    const byteCharacters = atob(base64);
    const byteNumbers = new Array(byteCharacters.length);
    
    for (let i = 0; i < byteCharacters.length; i++) {
        byteNumbers[i] = byteCharacters.charCodeAt(i);
    }
    
    const byteArray = new Uint8Array(byteNumbers);
    return new Blob([byteArray], {type: contentType});
}

function formatFileSize(bytes) {
    if (bytes < 1024) return bytes + ' B';
    if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(2) + ' KB';
    return (bytes / (1024 * 1024)).toFixed(2) + ' MB';
}

// Connect on page load
connect();