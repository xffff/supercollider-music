/************************************	*/
/* Ensemble Piece		             	*/
/*	Sequencer						*/
/************************************	*/

(
~stop_all = {
	~sequencer_stream.stop;
	~osc_destination.sendMsg("all_notes_off");
};
~stop_all.fork; 

~sequencer = Pseq([
	Pfuncn({"________________".postln; 0 }, 1),
	Pfuncn({"Section 0".postln; 0 }, 1),
	~sections[0],
	Pfuncn({"Section 1".postln; 0 }, 1),
	~sections[1],
	Pfuncn({"Section 2".postln; 0 }, 1),
	~sections[2],
	Pfuncn({"Section 3".postln; 0 }, 1),
	~sections[3],
	Pfuncn({"Section 4".postln; 0 }, 1),
	~sections[4],
	Pfuncn({"Section 5".postln; 0 }, 1),
	~sections[5],
	Pfuncn({"Section 6".postln; 0 }, 1),
	~sections[6],
	Pfuncn({"Section 7".postln; 0 }, 1),
	~sections[7],
	Pfuncn({"Section 8".postln; 0 }, 1),
	~sections[8],
	Pfuncn({"Section 9".postln; 0 }, 1),
	~sections[9],
	Pfuncn({"Section 10".postln; 0 }, 1),
	~sections[10],
	Pfuncn({"End!".postln; ~clock.stop; 0 }, 1)
], 1);
)
