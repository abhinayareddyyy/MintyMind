const cursor = document.getElementById('customCursor');
let mouseX = 0, mouseY = 0;
let cursorX = 0, cursorY = 0;

document.addEventListener('mousemove', (e) => {
    mouseX = e.clientX;
    mouseY = e.clientY;
});

function updateCursor() {
    cursorX += (mouseX - cursorX) * 0.25;
    cursorY += (mouseY - cursorY) * 0.25;
    cursor.style.left = (cursorX - 12) + 'px';
    cursor.style.top = (cursorY - 12) + 'px';
    requestAnimationFrame(updateCursor);
}
updateCursor();

document.addEventListener('mousedown', () => cursor.classList.add('active'));
document.addEventListener('mouseup', () => cursor.classList.remove('active'));

const AppData = {
    tasks: [],
    completedToday: [],
    streak: 0,

    init() {
        this.loadFromStorage();
        this.setDefaultDeadline();
    },

    loadFromStorage() {
        const stored = localStorage.getItem('mintymindData');
        if (stored) {
            const data = JSON.parse(stored);
            this.tasks = data.tasks || [];
            this.completedToday = data.completedToday || [];
            this.streak = data.streak || 0;
        }
    },

    saveToStorage() {
        localStorage.setItem('mintymindData', JSON.stringify({
            tasks: this.tasks,
            completedToday: this.completedToday,
            streak: this.streak
        }));
    },

    setDefaultDeadline() {
        const today = new Date();
        const year = today.getFullYear();
        const month = String(today.getMonth() + 1).padStart(2, '0');
        const date = String(today.getDate()).padStart(2, '0');
        document.getElementById('deadline').value = `${year}-${month}-${date}`;
    },

    addTask(data) {
        const task = {
            id: Date.now(),
            name: data.name,
            timeRequired: parseFloat(data.timeRequired),
            deadline: data.deadline,
            priority: parseInt(data.priority)
        };
        this.tasks.push(task);
        this.saveToStorage();
        return task;
    },

    getTodaysTasks() {
        const today = new Date().toISOString().split('T')[0];
        return this.tasks.filter(t => t.deadline === today);
    },

    completeTask(taskId) {
        if (!this.completedToday.includes(taskId)) {
            this.completedToday.push(taskId);
            this.saveToStorage();
        }
    },

    uncompleteTask(taskId) {
        this.completedToday = this.completedToday.filter(id => id !== taskId);
        this.saveToStorage();
    },

    deleteTask(taskId) {
        this.tasks = this.tasks.filter(t => t.id !== taskId);
        this.completedToday = this.completedToday.filter(id => id !== taskId);
        this.saveToStorage();
    }
};

const Sorting = {
    mergeSort(arr) {
        if (arr.length <= 1) return arr;
        const mid = Math.floor(arr.length / 2);
        return this.merge(this.mergeSort(arr.slice(0, mid)), this.mergeSort(arr.slice(mid)));
    },

    merge(left, right) {
        const result = [];
        let i = 0, j = 0;
        while (i < left.length && j < right.length) {
            if (left[i].priority > right[j].priority) {
                result.push(left[i++]);
            } else {
                result.push(right[j++]);
            }
        }
        return result.concat(left.slice(i)).concat(right.slice(j));
    },

    quickSort(arr) {
        if (arr.length <= 1) return arr;
        const pivot = arr[Math.floor(arr.length / 2)];
        const left = arr.filter(el => el.priority > pivot.priority);
        const middle = arr.filter(el => el.priority === pivot.priority);
        const right = arr.filter(el => el.priority < pivot.priority);
        return this.quickSort(left).concat(middle).concat(this.quickSort(right));
    }
};

function updateDashboard() {
    const todaysTasks = AppData.getTodaysTasks();
    const completed = AppData.completedToday.length;
    const percentage = todaysTasks.length > 0 ? Math.round((completed / todaysTasks.length) * 100) : 0;

    document.getElementById('taskCount').textContent = todaysTasks.length;
    document.getElementById('completedCount').textContent = completed;
    document.getElementById('streakCount').textContent = AppData.streak;
    document.getElementById('progressFill').style.width = percentage + '%';
    document.getElementById('progressText').textContent = percentage + '% complete';

    if (todaysTasks.length > 0 && completed === todaysTasks.length) {
        document.getElementById('completionMessage').style.display = 'block';
    } else {
        document.getElementById('completionMessage').style.display = 'none';
    }
}

function renderTodaysTodos() {
    const todaysTasks = AppData.getTodaysTasks();
    const container = document.getElementById('todayTasksList');
    container.innerHTML = '';

    if (todaysTasks.length === 0) {
        container.innerHTML = '<p style="text-align: center; opacity: 0.6; padding: 40px;">No tasks for today.</p>';
        return;
    }

    todaysTasks.forEach(task => {
        const isComplete = AppData.completedToday.includes(task.id);
        const div = document.createElement('div');
        div.className = 'task-item';
        div.innerHTML = `
            <div>
                <div class="task-title">${task.name}</div>
                <div class="task-meta">
                    <span>${task.timeRequired}h</span>
                    <span>Priority ${task.priority}</span>
                </div>
            </div>
            <input type="checkbox" ${isComplete ? 'checked' : ''} onchange="toggleComplete(${task.id})" style="width: 24px; height: 24px; cursor: pointer; accent-color: #4F7D2C;">
        `;
        container.appendChild(div);
    });
}

function renderSchedule() {
    const todaysTasks = AppData.getTodaysTasks();
    const container = document.getElementById('scheduleContainer');
    container.innerHTML = '';

    if (todaysTasks.length === 0) {
        container.innerHTML = '<p style="text-align: center; opacity: 0.6; padding: 40px;">No tasks scheduled for today.</p>';
        return;
    }

    const sorted = Sorting.mergeSort([...todaysTasks]);
    let hour = 9;

    sorted.forEach(task => {
        const div = document.createElement('div');
        div.className = 'task-item';
        const endHour = hour + task.timeRequired;
        div.innerHTML = `
            <div>
                <div class="task-title">${task.name}</div>
                <div class="task-meta">
                    <span>${hour.toFixed(1)} - ${endHour.toFixed(1)}</span>
                    <span>${task.timeRequired}h duration</span>
                    <span>Priority ${task.priority}</span>
                </div>
            </div>
        `;
        container.appendChild(div);
        hour = endHour;
    });
}

function toggleComplete(taskId) {
    if (AppData.completedToday.includes(taskId)) {
        AppData.uncompleteTask(taskId);
    } else {
        AppData.completeTask(taskId);
    }
    updateDashboard();
    renderTodaysTodos();
}

function performSorting() {
    const tasks = [...AppData.getTodaysTasks()];
    if (tasks.length === 0) {
        document.getElementById('sortingResults').innerHTML = '<p style="opacity: 0.6;">No tasks to sort.</p>';
        return;
    }

    const sorted = Sorting.mergeSort(tasks);
    let html = `<h4>Task Sorting Results</h4><ol>`;

    sorted.forEach(t => {
        html += `<li><strong>${t.name}</strong> - Priority ${t.priority}, ${t.timeRequired}h</li>`;
    });

    html += '</ol>';
    document.getElementById('sortingResults').innerHTML = html;
}

function updateScheduleOptimization() {
    const tasks = AppData.getTodaysTasks();
    if (tasks.length === 0) {
        document.getElementById('scheduleOptimization').innerHTML = '<p style="opacity: 0.6;">No tasks to optimize.</p>';
        return;
    }

    const sorted = Sorting.mergeSort([...tasks]);
    let total = 0;
    const scheduled = [];

    sorted.forEach(t => {
        if (total + t.timeRequired <= 8) {
            scheduled.push(t);
            total += t.timeRequired;
        }
    });

    let html = `<p style="opacity: 0.85;">Scheduled ${scheduled.length} of ${tasks.length} tasks</p><p style="opacity: 0.85; margin-top: 10px;">Total time: ${total.toFixed(1)}h / 8h available</p>`;
    document.getElementById('scheduleOptimization').innerHTML = html;
}


document.getElementById('taskForm').addEventListener('submit', (e) => {
    e.preventDefault();

    AppData.addTask({
        name: document.getElementById('taskName').value,
        timeRequired: document.getElementById('timeRequired').value,
        deadline: document.getElementById('deadline').value,
        priority: document.getElementById('priority').value
    });

    document.getElementById('taskForm').reset();

    const alert = document.getElementById('taskFormAlert');
    alert.innerHTML = '<div class="alert success">Task added successfully.</div>';
    alert.style.display = 'block';

    updateDashboard();
    renderSchedule();
    renderTodaysTodos();

    setTimeout(() => alert.style.display = 'none', 3000);
});

document.getElementById('sortBtn').addEventListener('click', () => performSorting());

function switchSection(index) {
    const sections = document.querySelectorAll('.section');
    const navBtns = document.querySelectorAll('.nav-btn');

    sections.forEach(s => s.classList.remove('active'));
    navBtns.forEach(b => b.classList.remove('active'));

    sections[index].classList.add('active');
    navBtns[index].classList.add('active');

    if (index === 3) renderSchedule();
    if (index === 4) renderTodaysTodos();
    if (index === 5) updateScheduleOptimization();

    window.scrollTo(0, 0);
}

document.getElementById('modeToggle').addEventListener('click', () => {
    document.body.classList.toggle('dark-mode');
    const isDark = document.body.classList.contains('dark-mode');
    document.getElementById('modeToggle').textContent = isDark ? 'Sun' : 'Moon';
    localStorage.setItem('mintymindDarkMode', isDark);
});

if (localStorage.getItem('mintymindDarkMode') === 'true') {
    document.body.classList.add('dark-mode');
    document.getElementById('modeToggle').textContent = 'Sun';
}

AppData.init();
updateDashboard();
renderSchedule();
renderTodaysTodos();
