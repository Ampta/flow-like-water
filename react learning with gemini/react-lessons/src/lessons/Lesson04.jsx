import { useState } from 'react'

function Lesson04() {
    // 1. Create a "State Variable" (count) and a "Setter Function" (setCount)
    // The (0) is the initial value.
    const [count, setCount] = useState(0);

    const handleClick = () => {
        // 2. Use the setter to update the value
        setCount(count + 1);
    }

    const handleDecrement = () => {
        setCount(count - 1);
    }

    return (
        <div>
            <h1>Lesson 4: State</h1>
            <p>Current Count: {count}</p>
            <button onClick={handleClick}>
                Increment
            </button>

            <button style={{ backgroundColor: "red" }} onClick={handleDecrement}>
                Decrement
            </button>
        </div>
    )
}

export default Lesson04
