function get(id) {
    return document.getElementById(id);
}

let testBtn = get("testBtn");
let submit = get("submit");
let message = get("success");
let error = get("error");
submit.disabled = true;

let select = get('type');
let initValue = select.value


function showMessage(msg, error) {
    message.className = error ? 'alert alert-danger' : 'alert alert-success'
    message.textContent = msg;
    message.style.display = "block"
    // 2秒后隐藏消息
    setTimeout(function () {
        message.style.display = 'none';
    }, 2000);
}

function onChange(val) {
    let port = get('port')
    let username = get('username')
    switch (val) {
        case "MYSQL":
            port.value = '3306';
            username.value = 'root'
            break
        case "ORACLE":
            port.value = '';
            username.value = ''
            break
        case "SQLSERVER":
            port.value = '';
            username.value = ''
            break
    }
}

onChange(initValue);
select.onchange = function () {
    onChange(select.value);
}

function getValue() {
    let url = get('url').value;
    let type = get('type').value;
    let port = get('port').value;
    let databaseName = get('databaseName').value;
    let username = get('username').value;
    let password = get('password').value;
    return {
        url: url,
        type: type,
        port: port,
        databaseName: databaseName,
        username: username,
        password: password,
    };
}


function post(url, param) {
    return new Promise((resolve, reject) => {
        let request = new XMLHttpRequest();
        request.open("POST", url, true);
        request.setRequestHeader("Content-type", "application/json");
        request.send(JSON.stringify(param));
        request.onreadystatechange = function () {
            if (request.readyState === 4) {
                if (request.status === 200) {
                    resolve(JSON.parse(request.responseText));
                } else {
                    reject(new Error('Request failed with status code ' + request.status));
                }
            }
        };
    });
}

async function getTables(param) {
    let res = await post('/document/getTables', param)
    if (res.success) {
        let tbody = get('tbody')
        for (let index in res.data) {
            let item = res.data[index];

            let row = document.createElement('tr')
            let td1 = document.createElement('td');
            let td2 = document.createElement('td');
            let td3 = document.createElement('td');

            // 设置 td 元素的 class 属性
            td1.classList.add('serial');
            td2.classList.add('name');
            td3.classList.add('comment');

            let arr = item.split(':');

            td1.textContent = tbody.rows.length + 1;
            td2.textContent = arr[0]
            td3.textContent = arr[1]
            row.appendChild(td1)
            row.appendChild(td2)
            row.appendChild(td3)
            tbody.appendChild(row)
        }
    } else {
        error.style.display = "block"
    }
}

testBtn.onclick = async function () {
    let param = getValue();
    let json = await post('/document/test', param);
    if (json.data) {
        showMessage(json.msg);
        await getTables(param);
        submit.disabled = false;
    } else {
        submit.disabled = true;
        showMessage(json.msg, true);
    }
}

submit.onclick = async function () {
    let param = {};
    param.instance = getValue();
    param.tables = [];
    let json = await post('/document/create', param);
    showMessage(json.msg);

    // 创建下载链接
    const link = document.createElement('a');
    link.href = '/document/download?fileName=' + json.data
    link.download = json.data;
    link.click();
}
