/************************************	*/
/* Untitled Piece		             	*/
/*	Sequencer				    		*/
/************************************	*/

(
~stop_all = {
	~sequencer_stream.stop;
	~osc_destination.sendMsg("all_notes_off");
};
~stop_all.fork;

~sequencer = Pseq([
	Pfuncn({"________________".postln; 0 }, 1),
	Pfuncn({"START!".postln; 0 }, 1),
	Pfuncn({"Section 0".postln; 0 }, 1),
	~sections[0],
	Pfuncn({"END!".postln; ~clock.stop; 0 }, 1)
], 1);
)
