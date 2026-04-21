# MintyMind - Algorithmic Task Planner

## Overview

MintyMind is an aesthetic task planning application that combines beautiful UI design with powerful algorithms to optimize daily productivity. It intelligently schedules tasks using advanced sorting and greedy algorithms, all wrapped in a soft, minimal pastel interface.

## Project Description

**MintyMind** is a web application designed to help users manage their daily tasks efficiently while maintaining an enjoyable user experience. The application leverages Design and Analysis of Algorithms (DAA) concepts to create smart task scheduling and optimization features.

### Key Features

- **Aesthetic UI**: Soft, minimal pastel design with custom cursor effects and floating cards
- **Task Management**: Add, edit, delete, and organize tasks with priorities and deadlines
- **Algorithmic Optimization**: Uses advanced algorithms for intelligent task scheduling:
  - Merge Sort for priority-based sorting (O(n log n))
  - Greedy algorithms for scheduling optimization
  - Graph traversal for task dependencies
- **Progress Tracking**: Monitor completed tasks and maintain productivity streaks
- **Real-time Visualization**: View algorithm performance and task metrics
- **Dark/Light Mode**: Toggle between Moon and Sun modes for comfortable viewing
- **Local Storage**: Persistent data storage across sessions

## Technology Stack

### Frontend
- **HTML5**: Semantic markup and structure
- **CSS3**: Modern styling with animations and custom design
- **JavaScript**: Dynamic interactivity and app logic

### Backend
- **Java**: Core algorithmic implementations
  - Task data structures
  - Sorting algorithms (Merge Sort)
  - Scheduling algorithms (Greedy approach)
  - Algorithm complexity analysis

## Project Structure

```
.
├── index.html        # Main HTML structure
├── styles.css        # CSS styling and animations
├── script.js         # JavaScript logic and interactivity
├── TaskPlanner.java  # Java backend with algorithms
└── README.md         # This file
```

## How It Works

### Task Structure
Each task contains:
- **ID**: Unique identifier
- **Name**: Task description
- **Time Required**: Hours needed to complete
- **Deadline**: Due date/time
- **Priority**: Task priority level (1-5)

### Algorithm Implementation

#### 1. **Merge Sort** (Priority Sorting)
- **Purpose**: Sorts tasks by priority in descending order
- **Time Complexity**: O(n log n)
- **Space Complexity**: O(n)
- Ensures high-priority tasks are scheduled first

#### 2. **Greedy Scheduling**
- **Purpose**: Optimizes task scheduling based on deadlines and priorities
- **Approach**: Selects the best task at each step considering time constraints
- **Benefit**: Maximizes productivity while meeting deadlines

## Usage

1. **Open the Application**: Open `index.html` in a web browser
2. **Add Tasks**: Use the task input form to create new tasks
3. **Set Priorities**: Assign priority levels (1 = Low, 5 = High)
4. **View Schedule**: The app automatically sorts and schedules tasks
5. **Track Progress**: Monitor completed tasks and maintain your streak
6. **Toggle Theme**: Use the Moon/Sun button to switch display modes

## Data Persistence

MintyMind uses browser's `localStorage` to save:
- List of active tasks
- Completed tasks for the day
- Productivity streak count

Data persists across browser sessions until manually cleared.

## Algorithm Complexity Analysis

| Algorithm | Time Complexity | Space Complexity | Use Case |
|-----------|-----------------|------------------|----------|
| Merge Sort | O(n log n) | O(n) | Priority-based sorting |
| Greedy Scheduling | O(n²) | O(n) | Task scheduling |
| Graph Traversal | O(V + E) | O(V) | Task dependencies |

## Design Philosophy

- **Minimalist**: Clean interface with minimal clutter
- **Aesthetic**: Soft pastel color scheme for reduced eye strain
- **Intuitive**: Self-explanatory UI elements and workflows
- **Performant**: Efficient algorithms for smooth user experience
- **Accessible**: Clear visual hierarchy and readable typography

## Future Enhancements

- [ ] Cloud synchronization across devices
- [ ] Collaborative task sharing
- [ ] Advanced analytics and insights
- [ ] Calendar integration
- [ ] Mobile application
- [ ] Custom algorithm selection
- [ ] Task templates and recurring tasks
- [ ] Notification system

## Credits

This project was developed as part of the Design and Analysis of Algorithms (DAA) and Web Technologies course project.

## License

This project is created for educational purposes.

---

**Stay focused, stay productive with MintyMind!**
