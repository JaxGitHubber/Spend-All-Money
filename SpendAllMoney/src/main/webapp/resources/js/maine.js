class Item {
  constructor(name, image, price, isFavorite) {
    this.name = name;
    this.image = image;
    this.price = price;
    this.isFavorite = isFavorite;
  }
}

let isPeoplePanelActive = false;
function controlPeoplePanel() {
    if(!isPeoplePanelActive) {
	    showPeoplePanel()
	    isPeoplePanelActive = true;
	} else {
		hidePeoplePanel();
     	isPeoplePanelActive = false;
	}
}

function changePerson(img) {
	fetch('/SpendAllMoney/person', {
    method: 'PUT',
    body: img.alt
    });
    updatePage();
}

function hidePeoplePanel() {
	document.getElementById("people").style.visibility = "hidden";
}

function showPeoplePanel() {
	document.getElementById("people").style.visibility = "visible";
}

function accountController(account) {
	if(account.alt == "true") {
		accountMenu();
	} else {
		registration();
	}
}

let isAccountMenuOpen = false;
function accountMenu() {
	if(isAccountMenuOpen) {
		closeMenu();
		isAccountMenuOpen = false;
	} else {
		openMenu();
		isAccountMenuOpen = true;
	}
}

function openMenu() {
	document.getElementById("account_menu").style.visibility = "visible";
}

function closeMenu() {
	document.getElementById("account_menu").style.visibility = "hidden";
}

function registration() {
	window.location = "http://localhost:8081/SpendAllMoney/registration";
}

function signout() {
	fetch("/SpendAllMoney/signout", {
		method: 'DELETE'
	});
	updatePage();
}

function leaderboard() {
	window.location = "http://localhost:8081/SpendAllMoney/leaderboard";
}

let isHistoryPanelActive = false;
function controlHistory() {
	if(!isHistoryPanelActive) {
	    showHistory()
	    isHistoryPanelActive = true;
	} else {
		hideHistory();
     	isHistoryPanelActive = false;
	}
}

function hideHistory() {
	document.getElementById("history").style.visibility = "hidden";
	document.getElementById("history-button").src = "resources/images/history.png";
}

function showHistory() {
	document.getElementById("history").style.visibility = "visible";
	document.getElementById("history-button").src = "resources/images/reject.png";
}

function clearHistory() {
	fetch("/SpendAllMoney/history", {
		method: 'DELETE'
	});
	updatePage();
}

//TODO
function reset() {
  saveNewState(document.getElementById("fixedState").innerHTML + ";0$");
  clearCountBarriers();
  updatePage();
}

function updatePage() {
	setTimeout(function() {
		window.location = "http://localhost:8081/SpendAllMoney/main";
	}, 50);
}


function saveNewState(state) {
	fetch("/SpendAllMoney/state", {
		method: 'POST',
		body: state
	});
}

function clearCountBarriers() {
	fetch("/SpendAllMoney/items/barrier/clear", {
		method: 'POST'
	});
}

function minusCount(tag) {
  let info = tag.id.split(";");
  if(Number(document.getElementById(info[0]).innerHTML) > 0) {
    document.getElementById(info[0]).innerHTML = Number(document.getElementById(info[0]).innerHTML) - 1;
    removeItemFromShoppingCart(new Item(document.getElementById(info[2]).alt, null, document.getElementById(info[1]).innerHTML, false));
  }
}

function removeItemFromShoppingCart(item) {
	fetch('/SpendAllMoney/item', {
    method: 'DELETE',
    body: JSON.stringify(item)
    });
}

function plusCount(tag) {
  let info = tag.id.split(";");
    let CountBarrier = Number(tag.title);
    if(CountBarrier == -1) {
		CountBarrier = 999;
	}
    if(Number(document.getElementById(info[0]).innerHTML) < CountBarrier) {
      let info = tag.id.split(";");
      document.getElementById(info[0]).innerHTML = Number(document.getElementById(info[0]).innerHTML) + 1;
      addItemInShoppingCart(new Item(document.getElementById(info[2]).alt, "resources/images/" + info[2] + ".png", document.getElementById(info[1]).innerHTML, false));
  }
}

function addItemInShoppingCart(item) {
	fetch('/SpendAllMoney/item', {
    method: 'POST',
    body: JSON.stringify(item)
    });
}
