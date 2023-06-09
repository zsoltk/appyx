# Appyx Interactions – Gestures

{==

Gesture-based transitions are usually done by direct UI mutation. In Appyx, they're done by gradual model mutation instead.

==}

In Appyx, `TransitionModels` are capable of representing transient states as a `%` between two endpoint states such as `State(S1, S2, %)`.

This way, both animations and gestures operate directly on the same model state, rather than the UI. For example, a gesture would gradually increase the `%` value:


``` mermaid
flowchart LR
  M1[State 1] --> |"% gradual change"| M2[State 2];
```

For every frame of the animation, UI is function of the current model state, with the `%` value taken into account.  

Appyx does it this way, so that:

1. The model state remains to be a single source of truth and the UI is only a function of it – for every single frame in the transition  
2. There's no special code needed for making a gesture behave visually the same way as an animation between two states – both of them affect the underlying, single source of truth
3. Upon releasing a drag gesture, the model doesn't need to be updated about a new state – it already knows about it 


## Operations
