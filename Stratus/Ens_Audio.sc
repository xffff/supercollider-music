/************************************	*/
/* Ensemble Piece		             	*/
/*	AUDIO						*/
/************************************	*/

(
~cleanup = nil;
~start_audio = nil;
~input = nil; 
~fx = nil; 
~hala = nil;
~output = nil; 
~master_dry_bus = nil; 
~master_fx_bus = nil; 
~master_mix_bus = nil;
~buf_a = nil;
~dryaudio = nil;

fork{
	~cleanup = {
		postln("Cleaning up");
		~input.free; ~fx.free; ~hala.free; ~output.free;
		~master_dry_bus.free;
		~master_fx_bus.free;
		~master_mix_bus.free;
		Buffer.freeAll;
		~dryaudio.free;
		s.newAllocators;
		s.sync; 
		postln("Done cleaning up");
	};
	~cleanup.fork;
	
	0.05.wait; // make sure we're cleaned up before we do the rest
	
	~start_audio = {
		(
		// groups
		~input = Group.new(s,\addToHead);   	// from sampler
		~fx = Group.new(s, \addToTail);     	// fx chain
		~hala = Group.new(s, \addToTail);	// spatialisation
		~output = Group.new(s, \addToTail); 	// output
		postln("Groups Allocated");
		
		// busses
		~master_dry_bus  = Bus.audio(s,~numfxchans); 	// dry bus (dry -> fx)
		~master_fx_bus  = Bus.audio(s,32); 			// fx bus (fx -> fx)
		~master_mix_bus = Bus.audio(s,64); 			// mix bus (all -> output)
		postln("Busses Set");
		
		// buffers
		~tamtam_buf = Buffer.alloc(s, s.sampleRate * 16, 1); 
		~ctl_buf = Buffer.alloc(s, s.sampleRate * 16, 1);
		~sax_buf = Buffer.alloc(s, s.sampleRate * 2, 1);
		postln("Buffers Allocated");
		);
		
		////////////////////////////////////////////////////
		
		// start input
		(
		~dryaudio = Synth(\dryaudio, 
			[
			\in, #[0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15], 
			\amp, 1.0, 
			\out, ~master_dry_bus,
			], target: ~input, addAction: \addToHead);
			
			postln("Dry Audio On");
			s.queryAllNodes;
		);
		
	};
	~start_audio.fork; 
};
)